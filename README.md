
# FireDetection System

This project is a fire detection system built using a **Raspberry Pi 4** (RPI) and an **STM microcontroller** (ECU) connected via **SPI over CAN protocol**. The system measures the temperature using a **DHT11 sensor**, which is connected to the STM ECU. The Raspberry Pi 4 runs a custom Linux image built with **Yocto**, which includes device drivers for GPIO and CAN communication, along with **Python** and **Bash** scripts to handle various system tasks.


## Features

- **Temperature Detection**: Uses the DHT11 sensor to monitor temperature levels.
- **CAN Protocol Communication**: The Raspberry Pi and STM communicate using SPI over CAN.
- **Custom Yocto Image**: The Raspberry Pi runs a tailored image built using Yocto, with custom layers, recipes, and a distribution for optimized performance.
- **GPIO and CAN Drivers**: The project includes custom device drivers for GPIO control and CAN management.
- **Automated Scripts**: Python and Bash scripts handle data processing, communication, and system control.


## Hardware Setup
1. **Raspberry Pi 4**: Serves as the central processing unit, running the customized Linux image.
2. **STM Microcontroller (ECU)**: Connected to the Raspberry Pi via SPI, handles sensor data collection from the DHT11.
3. **DHT11 Sensor**: Measures the temperature, connected to the STM ECU.
4. **CAN Bus**: SPI over CAN protocol is used for communication between the Raspberry Pi and the STM ECU.


## Software Components

### 1. Yocto-Based Image


The Raspberry Pi image is built using **Yocto**, a powerful build system that allows creating custom Linux distributions for embedded systems. The image is tailored to meet the requirements of the fire detection system.


#### 1.1 Download the Layers and BSP

- Create directory called "**yocto**"

- Download Poky build system (Kirkstone branch)

```bash
git clone -b kirkstone git://git.yoctoproject.org/poky
```

Download RaspberryPi Board Support Package **BSP** (Kirkstone branch)

```bash
git clone -b kirkstone https://github.com/agherzan/meta-raspberrypi.git 
```


- Download openembedded (kirkstone branch)

```bash
git clone -b kirkstone https://github.com/openembedded/meta-openembedded.git
```

- Download meta-java (kirkstone branch)

##### Creating a customize Layer for the project 

- The layer contains the Distro for the Project and needed Recipes

```

mohamed@Elgaar:~/Desktop/Yocto/meta-firealarm$ tree
.
├── conf
│   ├── distro
│   │   └── firealarm.conf
│   └── layer.conf
├── COPYING.MIT
├── README
├── recipes-example
│   └── example
│       └── example_0.1.bb
├── recipes-google
│   └── google
│       └── myrecipe.bb
├── recipes-jdk17
│   └── jdk17
│       └── jdk17_0.1.bb
├── recipes-kernel
│   ├── ledmod
│   │   ├── build
│   │   │   └── conf
│   │   │       ├── bblayers.conf
│   │   │       ├── local.conf
│   │   │       └── templateconf.cfg
│   │   ├── files
│   │   │   ├── led_device1.c
│   │   │   ├── led_device2.c
│   │   │   ├── led_device3.c
│   │   │   ├── led_device4.c
│   │   │   ├── Makefile
│   │   │   ├── mydriver.c
│   │   │   └── spidriver.c
│   │   ├── ledmod_0.1.bb
│   │   └── spidriver_0.1.bb
│   └── linux-headers
│       └── linux-headers_0.1.bb
├── recipes-multimedia
│   └── mpv
│       ├── mpv_0.35.1.bb
│       └── mpv_0.35.1.bb.save
├── recipes-pingled
│   └── pingled
│       ├── files
│       │   ├── myservice.service
│       │   └── pingscript.sh
│       ├── myservice.service
│       ├── pingled.bb
│       └── pingscript.sh
├── recipes-rpigpio
│   └── rpigpio
│       └── python3-RPi.GPIO_0.1.bb
├── recipes-spidev-tools
│   └── spidev-tools
│       └── spidev-tools_0.1.bb
├── recipes-spidriver
│   └── spidriver
│       ├── files
│       │   └── spidriver.c
│       └── spidriver_0.1.bb
├── recipes-spitools
│   └── spi-tools
│       └── spi-tools_0.1.bb
└── recipes-wirelesstools
    └── wireless-tools
        └── wireless-tools_0.1.bb
```

- Setup the Environment 

```bash
source oe-init-build-env 
```
#### 1.2 Add the Layers in *bblayer.conf* file

```
BBLAYERS ?= " \
  /home/mohamed/Desktop/Yocto/poky/meta \
  /home/mohamed/Desktop/Yocto/poky/meta-poky \
  /home/mohamed/Desktop/Yocto/poky/meta-yocto-bsp \
  /home/mohamed/Desktop/Yocto/meta-firealarm \
  /home/mohamed/Desktop/Yocto/poky/meta-raspberrypi \
  /home/mohamed/Desktop/Yocto/poky/meta-openembedded/meta-oe \
  /home/mohamed/Desktop/Yocto/poky/meta-openembedded/meta-gnome \
  /home/mohamed/Desktop/Yocto/poky/meta-openembedded/meta-xfce \
  /home/mohamed/Desktop/Yocto/poky/meta-openembedded/meta-python \
  /home/mohamed/Desktop/Yocto/poky/meta-openembedded/meta-networking \
  /home/mohamed/Desktop/Yocto/poky/meta-openembedded/meta-multimedia \
  /home/mohamed/Desktop/Yocto/meta-java \
  "
```

#### 1.3 Configure Local.conf File

- Change `MACHINE` to the Wanted target 

```
MACHINE ??= "raspberrypi4-64"
```
- Change `DISTRO` to the our Distro in the customize Layer 

```
DISTRO ?= "firealarm"
```

##### the distro contains the features i want to add in the image and installed files 

firealarm.conf

```
include meta-poky/conf/distro/poky.conf


DISTRO = "firealarm"
DISTRO_NAME = "FireAlarm"
DISTRO_VERSION = "4.0.20"
DISTRO_CODENAME = "kirkstone"

CONF_VERSION = "2"

DISTRO_FEATURES += " systemd pulseaudio x11 audio-only "
VIRTUAL-RUNTIME_init_manager = "systemd"
VIRTUAL-RUNTIME_initscripts = "systemd-compat-units"
VIRTUAL-RUNTIME_login_manager = "shadow-base"
VIRTUAL-RUNTIME_dev_manager = "systemd"


PREFERRED_PROVIDER_virtual/javac-native = "ecj-bootstrap-native"

IMAGE_INSTALL:append = " packagegroup-core-buildessential "




MACHINE_FEATURES:append = " spi"
IMAGE_INSTALL:append = " spidev-tools"
IMAGE_INSTALL:append = " kernel-modules"
IMAGE_INSTALL:append = " python3-smbus"
TOOLCHAIN_TARGET_TASK:append = " gcc g++"

IMAGE_INSTALL:append = " pulseaudio speexdsp"
IMAGE_INSTALL:append = "  systemd systemd-serialgetty"
IMAGE_INSTALL:append = " gstreamer1.0-plugins-good gstreamer1.0-plugins-base gstreamer1.0-plugins-ugly alsa-utils alsa-lib "
LICENSE_FLAGS_ACCEPTED:append = " commercial commercial_gstreamer1.0-plugins-ugly"
PACKAGECONFIG:pn-qtmultimedia = " gstreamer alsa"

KERNEL_MODULE_AUTOLOAD:rpi = "snd-bcm2835"
MACHINE_FEATURES:append = "sound"

IMAGE_INSTALL:append = " pavucontrol pulseaudio pulseaudio-module-dbus-protocol pulseaudio-module-loopback pulseaudio-server  alsa-utils alsa-plugins packagegroup-rpi-test"

PREFERRED_VERSION_python3 = "3.10.14"
PREFERRED_VERSION_python3-dev = "3.10.14"
PREFERRED_PROVIDER_virtual/kernel = "linux-raspberrypi"



PREFERRED_VERSION_linux-raspberrypi = "6.1.77%"

IMAGE_INSTALL:append = " python3 python3-pip python3-dev \
  kernel-modules libxkbui \
  python3-spidev canutils i2c-tools spi-tools\
  make gcc dpkg apt"

MAGE_INSTALL:append = " \
    python3 \
    util-linux \
    bluez5 \
    i2c-tools \
    bridge-utils \
    hostapd \
    iptables \
    wpa-supplicant \
    pi-bluetooth \
    bluez5-testtools \
    udev-rules-rpi \
    linux-firmware \
    iw \
    kernel-modules \
    linux-firmware-ralink \
    linux-firmware-rtl8192ce \
    linux-firmware-rtl8192cu \
    linux-firmware-rtl8192su \
    linux-firmware-rpidistro-bcm43430 \
    linux-firmware-bcm43430 \
    connman \
    connman-client \
    dhcpcd \
    openssh \
    psplash \
    psplash-raspberrypi \
    coreutils \
"

DISTRO_FEATURES:append = " \
    bluez5 \
    bluetooth \
    wifi \
    pi-bluetooth \
    linux-firmware-bcm43430 \
    systemd \
    usrmerge \
    ipv4 \
"

MACHINE_FEATURES:append = " \
    bluetooth \
    wifi \
"

IMAGE_FEATURES:append = " \
    splash \
"
IMAGE_INSTALL:append = " xserver-xorg xf86-video-fbdev xf86-input-evdev xterm matchbox-wm"


MAGE_INSTALL:append = " device-tree-overlays"

IMAGE_INSTALL:append = " setserial"

IMAGE_FSTYPES = "tar.xz ext3 rpi-sdimg"
INHERIT = "rm_work"
ENABLE_UART = "1"
ENABLE_SPI  = "1"

```

#### 1.4  Build the Image 


```bash
bitbake -k core-image-sato 
```

#### 1.5 Flash the Image on SD Card

```
 sudo dd if=./core-image-sato-raspberrypi4-20240907095335.rootfs.rpi-sdimg of=/dev/sdb bs=1M

```

### 2. Device Drivers

#### GPIO Driver:
A custom GPIO driver is included in the image to manage the general-purpose input/output pins on the Raspberry Pi.

## Overview

This kernel module is designed to manage two LED devices via a platform driver. It provides functionality to allocate GPIO pins, set their direction, and control the LEDs based on user input. The module creates a character device that allows user-space applications to interact with the LEDs.

## Features

Platform Driver: Handles two LED devices (LED1 and LED2) using platform device bindings.
GPIO Control: Allocates and manages GPIO pins for the LEDs.
Character Device: Implements basic file operations (open, read, write, release) to control LEDs from user space.

## Module Component

**1.Device Initialization and Registration**:

1. GPIO Allocation: Allocates GPIO pins 2 and 3 for LED1 and LED2, respectively.

2. Device Creation: Creates character device nodes for each LED.

**2.File Operations**:

1. Open: Initializes the device when it is opened.

2. Close: Handles cleanup when the device is closed.

3. Read: Placeholder function, currently does nothing.

4. Write: Controls the LED state based on user input (0 to turn off, 1 to turn on).

**3.Cleanup**:

1. GPIO Deallocation: Frees GPIO pins when the module is unloaded.

2. Device Removal: Destroys device nodes and unregisters the character device.




## Building the Module

1. Ensure Kernel Headers are Installed:
```sh
sudo apt-get install linux-headers-$(uname -r)


```

2. Build the Module:Navigate to the directory containing your Makefile and run:

```sh
make
```

3. Loading the Module

```sh 

sudo insmod mydriver.ko 

sudo insmod platform_device1.ko

sudo insmod platform_device2.ko

```

4. check kernel logs
```sh

dmesg | tail
```
5. also verify that devices has created

```sh
ls /dev | grep LED
```

## use it with board has gpio interface

1.Write to the Device:
```sh
echo 1 > /dev/LED1  
echo 0 > /dev/LED2  

```

## Unloading the Module

```sh

sudo rmmod platform_device1

```
1. check kernel logs
```sh

dmesg | tail
```
2. also verify that devices has been destroyed

```sh
ls /dev | grep LED
```



#### CAN Manager Driver:
A custom driver was written to handle the CAN bus communication. It manages the SPI protocol over the CAN interface to communicate with the STM ECU.

# CAN Communication Using MCP2515 (SPI) and SocketCAN

## Overview

This repository contains kernel modules demonstrating CAN (Controller Area Network) communication using two methods:
1. *SPI-based CAN Communication*: Utilizes the MCP2515 CAN controller connected via SPI.
2. *SocketCAN Communication*: Utilizes the SocketCAN interface available on Linux systems for CAN communication.

## SPI-based CAN Communication with MCP2515

### Description

This module demonstrates how to interface with the MCP2515 CAN controller over SPI. It includes functions for:
- Reading and writing to MCP2515 registers
- Resetting the MCP2515
- Configuring bit timing
- Setting operation modes
- Receiving CAN messages

### Prerequisites

- Linux kernel with SPI support enabled.
- MCP2515 CAN controller connected via SPI.
- SPI device configured in the device tree or as a kernel module.

### Building the Module

1. Create a Makefile to compile the module.
2. Build the module using make.

### Loading the Module

1. Insert the module using insmod.
2. Check the kernel log for output using dmesg or cat /var/log/kern.log.

### Usage

- The module initializes the MCP2515, configures it, and prints received CAN messages to the kernel log.
- To simulate receiving CAN messages, the module will log received data upon initialization.

## CAN Communication Using SocketCAN

### Description

This module demonstrates CAN communication using the SocketCAN interface on Linux. It includes functionality for:
- Sending CAN frames
- Receiving CAN frames via a notifier block

### Prerequisites

- Linux kernel with SocketCAN support enabled.
- CAN interface (e.g., can0) set up and configured on your system.

### Building the Module

1. Create a Makefile to compile the module.
2. Build the module using make.

### Loading the Module

1. Insert the module using insmod.
2. Ensure that the CAN interface (e.g., can0) is configured and up.
3. Check the kernel log for output using dmesg or cat /var/log/kern.log.

### Usage

- The module will send a sample CAN message upon initialization.
- It will log received CAN frames to the kernel log.

### Configuration

- Replace "can0" with the appropriate CAN interface name as needed in the source code.





### 3. Adding JavaFX to rpi image

- By running this command we get Basic Dependency Analysis of a JAR file :

```
jdeps myapp.jar

```

- Download the needed JAVAFX Classes 
- Then add them in the `/usr/share/java`

- then add them with the running jar file

```
java --module-path /usr/share/java --add-modules javafx.controls,javafx.fxml -cp /usr/share/java/javafx-base-11.jar:/usr/share/java/javafx-controls-11.jar:/usr/share/java/javafx-graphics-11.jar:/usr/share/java/javafx-media-11.jar:/usr/share/java/javafx-web-11.jar:/usr/share/java/javafx-fxml-11.jar:/usr/share/java/javafx-swing-11.jar:FireSystem.jar FireSystem
```


### 4. Fire Alarm Application 

This Java-based fire detection system monitors multiple zones for potential fire hazards. Each zone operates as a separate thread, monitoring temperature through input streams and controlling an LED and buzzer 

alarm system. The application consists of multiple scenes:

1. Intro Scene: Displays an introductory animation (GIF).

2. Login Page: User must log in using a valid username and password.

3. Configuration Menu: Allows the user to set the fire temperature threshold for each zone. If any zone is left without a setting, it will be assigned a default value.

4. Main Scene: Displays the status of each zone and allows monitoring of fire hazards. If a fire is detected in any zone, the alarm sounds, and the LED for the respective zone lights up. A "Silence" button is

provided to stop the alarm.


#### Application Flow

1. Intro Scene: After the intro animation completes, the application transitions to the login page.

2. Login Page: Requires a username and password to proceed to the next step. On successful login, the configuration menu is displayed.

3. Configuration Menu: The user sets a fire temperature threshold for each zone. If any zone is left without a specified value, the system prompts the user, allowing them to assign a default temperature. The main monitoring scene is launched afterward.

4. Main Scene: In the main scene, each zone is represented by an instance of the Zone class, which monitors the temperature and triggers the alarm and LED if a fire is detected. The GUI shows which zone is on fire.

#### Zone Class

The Zone class is a thread that monitors a specific zone:

- zoneTitle: Name of the zone.
- ledPath: Path to control the LED for this zone.
- tempPath: Path to read temperature data.
- setTemp: Temperature threshold to detect a fire.
- currentTemp: Current temperature reading.
- fireState: Indicates if the zone is in a fire state.
- sensorState: Determines if the sensor is functional.
- error: Tracks if there is an error in the zone.
- canDown and canError: Flags for managing communication and error states for the zone.
- Each zone monitors the temperature through input streams and controls the alarm and LED using output streams.

Each zone monitors the temperature through input streams and controls the alarm and LED using output streams.




