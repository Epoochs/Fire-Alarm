KDIR := $(KERNEL_SRC)
PWD := $(shell pwd)

obj-m += mydriver.o led_device1.o led_device2.o led_device3.o led_device4.o

all:
	$(MAKE) -C $(KDIR) M=$(PWD) modules

clean:
	$(MAKE) -C $(KDIR) M=$(PWD) clean

install:
	install -d $(DESTDIR)/lib/modules/$(shell uname -r)/kernel/drivers/ledmod
	install -m 0644 *.ko $(DESTDIR)/lib/modules/$(shell uname -r)/kernel/drivers/ledmod
