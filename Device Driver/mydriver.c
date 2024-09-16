#include <linux/cdev.h>
#include <linux/device.h>
#include <linux/fs.h>
#include <linux/gpio.h>
#include <linux/init.h>
#include <linux/module.h>
#include <linux/platform_device.h>
#include <linux/uaccess.h>
#include <linux/usb.h>

#define DRIVER_NAME "zidandriver"
#define DEVICE_SIZE 1024

dev_t mydevice_id;
static char led1[3] = {0};
static char led2[3] = {0};
static char led3[3] = {0};
static char led4[3] = {0};
char device[DEVICE_SIZE];

enum devv { LED1 = 2, LED2 = 3, LED3 = 4, LED4 = 5 } Status;

struct class *iti_class;
struct cdev device_driver1;

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Zidan");
MODULE_DESCRIPTION("A Simple Kernel Module Supporting 4 LED Devices");

// Probe function (called every time a LED device is connected on platform bus)
int prob_device(struct platform_device *LED) {
  printk("%s device detected\n", LED->name);

  if (strcmp(LED->name, "LED1") == 0) {
    if (gpio_request(2, "FOR LED1")) {
      printk("cannot allocate gpio 2\n");
    } else {
      printk("Gpio pin 2 allocated successfully\n");
    }
    if (gpio_direction_output(2, 0)) {
      printk("cannot set gpio 2 direction as output\n");
      gpio_free(2);
    } else {
      printk("gpio 2 direction set successfully\n");
    }
  } else if (strcmp(LED->name, "LED2") == 0) {
    if (gpio_request(3, "FOR LED2")) {
      printk("cannot allocate gpio 3\n");
    } else {
      printk("Gpio pin 3 allocated successfully\n");
    }
    if (gpio_direction_output(3, 0)) {
      printk("cannot set gpio 3 direction as output\n");
      gpio_free(3);
    } else {
      printk("gpio 3 direction set successfully\n");
    }
  } else if (strcmp(LED->name, "LED3") == 0) {
    if (gpio_request(4, "FOR LED3")) {
      printk("cannot allocate gpio 4\n");
    } else {
      printk("Gpio pin 4 allocated successfully\n");
    }
    if (gpio_direction_output(4, 0)) {
      printk("cannot set gpio 4 direction as output\n");
      gpio_free(4);
    } else {
      printk("gpio 4 direction set successfully\n");
    }
  } else if (strcmp(LED->name, "LED4") == 0) {
    if (gpio_request(5, "FOR LED4")) {
      printk("cannot allocate gpio 5\n");
    } else {
      printk("Gpio pin 5 allocated successfully\n");
    }
    if (gpio_direction_output(5, 0)) {
      printk("cannot set gpio 5 direction as output\n");
      gpio_free(5);
    } else {
      printk("gpio 5 direction set successfully\n");
    }
  }

  if (NULL ==
      device_create(iti_class, NULL, mydevice_id + LED->id, NULL, LED->name)) {
    printk("Device can not be created");
  } else {
    printk("Device %s created successfully\n", LED->name);
  }
  return 0;
}

// Invoked when device is removed from bus (unloaded with rmmod)
int device_remove(struct platform_device *LED) {
  if (strcmp(LED->name, "LED1") == 0) {
    gpio_set_value(2, 0);
    gpio_free(2);
  } else if (strcmp(LED->name, "LED2") == 0) {
    gpio_set_value(3, 0);
    gpio_free(3);
  } else if (strcmp(LED->name, "LED3") == 0) {
    gpio_set_value(4, 0);
    gpio_free(4);
  } else if (strcmp(LED->name, "LED4") == 0) {
    gpio_set_value(5, 0);
    gpio_free(5);
  }
  device_destroy(iti_class, mydevice_id + LED->id);
  return 0;
}

// Devices managed by the driver on the platform bus
struct platform_device_id device_id[4] = {[0] = {.name = "LED1"},
                                          [1] = {.name = "LED2"},
                                          [2] = {.name = "LED3"},
                                          [3] = {.name = "LED4"}};

// Driver data (probe function, remove function)
struct platform_driver platform_driver_data = {.probe = prob_device,
                                               .remove = device_remove,
                                               .id_table = device_id,
                                               .driver = {.name = "mydriver"}};

////////////////////////////////// File Operations
/////////////////////////////////////

int driver_open(struct inode *device_file, struct file *instance) {
  int minor = MINOR(device_file->i_rdev);
  int major = MAJOR(device_file->i_rdev);
  instance->private_data = &minor;
  printk("Opened device with major number %d and minor number %d\n", major,
         minor);
  return 0;
}

int driver_close(struct inode *device_file, struct file *instance) {
  printk("Device was closed\n");
  return 0;
}

ssize_t driver_read(struct file *file, char __user *userr, size_t count,
                    loff_t *) {
  printk("Read function invoked\n");
  return -ENOSYS;
}

ssize_t driver_write(struct file *file, const char __user *userr, size_t count,
                     loff_t *) {
  printk("Write function invoked\n");
  char(*value)[3] = NULL;
  int not_copied;

  int minor = *(int *)file->private_data;
  switch (minor) {
  case 0:
    value = &led1;
    Status = LED1;
    break;
  case 1:
    value = &led2;
    Status = LED2;
    break;
  case 2:
    value = &led3;
    Status = LED3;
    break;
  case 3:
    value = &led4;
    Status = LED4;
    break;
  default:
    printk("Invalid device minor number\n");
    return -ENODEV;
  }

  not_copied = copy_from_user(value, userr, 3);
  switch (*value[0]) {
  case '0':
    printk("LED OFF\n");
    gpio_set_value(Status, 0);
    break;
  case '1':
    printk("LED ON\n");
    gpio_set_value(Status, 1);
    break;
  default:
    printk("Invalid input\n");
    break;
  }

  count = count - not_copied;
  return count;
}

/////////////////////////////////////////////////////////////////////////////////////

static struct file_operations file_op = {
    .owner = THIS_MODULE,
    .open = driver_open,
    .release = driver_close,
    .read = driver_read,
    .write = driver_write,
};

static int __init lkm_init(void) {
  if (alloc_chrdev_region(&mydevice_id, 0, 1, DRIVER_NAME) < 0) {
    printk(KERN_ALERT "Failed to allocate device number\n");
  } else {
    printk("Device Major %d, Minor %d\n", MAJOR(mydevice_id),
           MINOR(mydevice_id));
  }

  if (NULL == (iti_class = class_create(THIS_MODULE, "iti_class_2"))) {
    printk("Class cannot be created\n");
  }

  cdev_init(&device_driver1, &file_op);
  if (cdev_add(&device_driver1, mydevice_id, 1) == -1) {
    printk("cdev add failed\n");
  }

  platform_driver_register(&platform_driver_data);
  return 0;
}

static void __exit lkm_exit(void) {
  platform_driver_unregister(&platform_driver_data);
  class_destroy(iti_class);
  cdev_del(&device_driver1);
  unregister_chrdev_region(&mydevice_id, 1);
  printk("Driver exit\n");
}

module_init(lkm_init);
module_exit(lkm_exit);
