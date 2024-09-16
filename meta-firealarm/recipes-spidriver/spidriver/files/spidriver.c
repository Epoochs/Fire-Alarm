/*************************//**
*  \file       spi_driver.c
*
*  \details    Simple SPI Linux device driver (File Operations)
*
*  \author     mohamed samir
*
*  \Tested with Linux Raspberry Pi 5.10.27-v7l+
***************************/
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/module.h>
#include <linux/kdev_t.h>
#include <linux/fs.h>
#include <linux/err.h>
#include <linux/cdev.h>
#include <linux/device.h>
#include <linux/spi/spi.h>
#include <linux/uaccess.h>

#define DEVICE_NAME "spi_device"
#define SPI_SPEED_HZ 8000000  // 8 MHz
#define SPI_MODE SPI_MODE_0

dev_t dev = 0;
static struct class *dev_class;
static struct cdev spi_cdev;
static struct spi_device *spi_dev;

/*
** Function Prototypes
*/
static int      __init spi_driver_init(void);
static void     __exit spi_driver_exit(void);
static int      spi_open(struct inode *inode, struct file *file);
static int      spi_release(struct inode *inode, struct file *file);
static ssize_t  spi_read(struct file *filp, char __user *buf, size_t len, loff_t *off);
static ssize_t  spi_write(struct file *filp, const char __user *buf, size_t len, loff_t *off);

static struct file_operations fops =
{
    .owner      = THIS_MODULE,
    .read       = spi_read,
    .write      = spi_write,
    .open       = spi_open,
    .release    = spi_release,
};

/*
** This function will be called when we open the Device file
*/
static int spi_open(struct inode *inode, struct file *file)
{
    pr_info("SPI Driver Open Function Called...!!!\n");
    return 0;
}

/*
** This function will be called when we close the Device file
*/
static int spi_release(struct inode *inode, struct file *file)
{
    pr_info("SPI Driver Release Function Called...!!!\n");
    return 0;
}

/*
** This function will be called when we read the Device file
*/
static ssize_t spi_read(struct file *filp, char __user *buf, size_t len, loff_t *off)
{
    pr_info("SPI Driver Read Function Called...!!!\n");
    return 0;
}

/*
** This function will be called when we write to the Device file
*/
static ssize_t spi_write(struct file *filp, const char __user *buf, size_t len, loff_t *off)
{
    char data[1];
    if (copy_from_user(data, buf, 1)) {
        return -EFAULT;
    }

    struct spi_message msg;
    struct spi_transfer transfer = {
        .tx_buf = data,
        .len = 1,
    };

    spi_message_init(&msg);
    spi_message_add_tail(&transfer, &msg);
    spi_sync(spi_dev, &msg);

    pr_info("Data written to SPI device: %x\n", data[0]);
    return len;
}

/*
** Module Init function
*/
static int __init spi_driver_init(void)
{
    int result;
    struct spi_board_info spi_board_info = {
        .modalias = DEVICE_NAME,
        .max_speed_hz = SPI_SPEED_HZ,
        .mode = SPI_MODE,
    };

    struct spi_master *master = spi_busnum_to_master(0);
    if (!master) {
        pr_err("SPI master not found\n");
        return -ENODEV;
    }

    spi_dev = spi_new_device(master, &spi_board_info);
    if (!spi_dev) {
        pr_err("Failed to create SPI device\n");
        return -ENODEV;
    }

    /* Allocating Major number */
    if ((alloc_chrdev_region(&dev, 0, 1, DEVICE_NAME)) < 0) {
        pr_err("Cannot allocate major number\n");
        spi_unregister_device(spi_dev);
        return -1;
    }
    pr_info("Major = %d Minor = %d\n", MAJOR(dev), MINOR(dev));

    /* Creating cdev structure */
    cdev_init(&spi_cdev, &fops);

    /* Adding character device to the system */
    if ((cdev_add(&spi_cdev, dev, 1)) < 0) {
        pr_err("Cannot add the device to the system\n");
        goto r_class;
    }

    /* Creating struct class */
    if (IS_ERR(dev_class = class_create(THIS_MODULE, DEVICE_NAME))) {
        pr_err("Cannot create the struct class\n");
        goto r_class;
    }

    /* Creating device */
    if (IS_ERR(device_create(dev_class, NULL, dev, NULL, DEVICE_NAME))) {
        pr_err("Cannot create the Device\n");
        goto r_device;
    }
    pr_info("Device Driver Insert...Done!!!\n");
    return 0;

r_device:
    class_destroy(dev_class);
r_class:
    unregister_chrdev_region(dev, 1);
    return -1;
}

/*
** Module exit function
*/
static void __exit spi_driver_exit(void)
{
    device_destroy(dev_class, dev);
    class_destroy(dev_class);
    cdev_del(&spi_cdev);
    unregister_chrdev_region(dev, 1);
    spi_unregister_device(spi_dev);
    pr_info("Device Driver Remove...Done!!!\n");
}

module_init(spi_driver_init);
module_exit(spi_driver_exit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Mohamed Samir");
MODULE_DESCRIPTION("Simple SPI Linux device driver (File Operations)");
MODULE_VERSION("1.0");