#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/netdevice.h>
#include <linux/can.h>
#include <linux/can/dev.h>
#include <linux/skbuff.h>
#include <linux/if_arp.h>
#include <linux/net.h>

MODULE_LICENSE("GPL");
MODULE_AUTHOR("mohamed samir");
MODULE_DESCRIPTION("CAN Send and Receive Kernel Module");
MODULE_VERSION("1.0");

// CAN device pointer
static struct net_device *can_dev;

// Function to send a CAN frame
static int send_can_message(struct net_device *dev, u32 can_id, u8 *data, u8 len)
{
    struct can_frame frame;
    struct sk_buff *skb;
    int err;

    // Allocate an sk_buff (socket buffer)
    skb = alloc_skb(sizeof(struct can_frame), GFP_KERNEL);
    if (!skb) {
        printk(KERN_ERR "Failed to allocate sk_buff\n");
        return -ENOMEM;
    }

    // Prepare the CAN frame
    frame.can_id = can_id;  // Set CAN ID
    frame.can_dlc = len;    // Data Length Code (DLC)
    memcpy(frame.data, data, len); // Copy data

    // Add CAN frame to skb
    skb_put_data(skb, &frame, sizeof(frame));

    // Set the network device
    skb->dev = dev;

    // Send the frame
    err = dev_queue_xmit(skb);
    if (err < 0) {
        printk(KERN_ERR "Failed to send CAN frame\n");
        return err;
    }

    printk(KERN_INFO "CAN frame sent: ID=0x%X, Data=[%X %X %X %X %X %X %X %X]\n", 
            frame.can_id, frame.data[0], frame.data[1], frame.data[2], frame.data[3], 
            frame.data[4], frame.data[5], frame.data[6], frame.data[7]);

    return 0;
}

// Function to receive a CAN frame (network device notifier)
static int receive_can_message(struct notifier_block *nb, unsigned long event, void *ptr)
{
    struct net_device *dev = netdev_notifier_info_to_dev(ptr);
    struct sk_buff *skb;
    struct can_frame *frame;

    // Ensure the event is for a CAN device and it's a receive event
    if (dev->type == ARPHRD_CAN && event == NETDEV_RX_HANDLER) {
        // Receive the CAN frame
        skb = dev_alloc_skb(sizeof(struct can_frame));
        if (!skb) {
            printk(KERN_ERR "Failed to allocate sk_buff for receiving CAN frame\n");
            return NOTIFY_DONE;
        }

        frame = (struct can_frame *)skb->data;

        // Print received data
        printk(KERN_INFO "Received CAN frame: ID=0x%X, Data=[%X %X %X %X %X %X %X %X]\n",
               frame->can_id, frame->data[0], frame->data[1], frame->data[2], frame->data[3],
               frame->data[4], frame->data[5], frame->data[6], frame->data[7]);

        return NOTIFY_OK;
    }

    return NOTIFY_DONE;
}

// Notifier block for receiving CAN messages
static struct notifier_block can_notifier = {
    .notifier_call = receive_can_message,
};

// Init function
static int __init can_module_init(void)
{
    int err;

    // Get CAN device (replace "can0" with your interface name)
    can_dev = dev_get_by_name(&init_net, "can0");
    if (!can_dev) {
        printk(KERN_ERR "CAN device not found\n");
        return -ENODEV;
    }

    // Register notifier for CAN message reception
    err = register_netdevice_notifier(&can_notifier);
    if (err) {
        printk(KERN_ERR "Failed to register CAN notifier\n");
        return err;
    }

    printk(KERN_INFO "CAN module initialized\n");

    // Example: Send a CAN message (ID=0x123, Data={0x11, 0x22, 0x33, 0x44})
    u8 data[] = {0x11, 0x22, 0x33, 0x44};
    send_can_message(can_dev, 0x123, data, sizeof(data));

    return 0;
}

// Exit function
static void __exit can_module_exit(void)
{
    unregister_netdevice_notifier(&can_notifier);

    if (can_dev)
        dev_put(can_dev);

    printk(KERN_INFO "CAN module unloaded\n");
}

// Register init and exit functions
module_init(can_module_init);
module_exit(can_module_exit);
