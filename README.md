# WPKG CLI

[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/it-works-why.svg)](https://forthebadge.com)

WPKG gui client for WPKG RAT's

# Building

## Requirements
- JDK
- Maven

To build run the command: `mvn clean package`

If you don't have Maven you can use Maven wrapper: `./mvnw clean package`

Jar file can be found in `target` directory

# Packaging
You can generate packages for this operating systems:
## Windows:

### Requirements:
Make sure you have installed NSIS
### Building:
If you are building on **Linux**: `./package/setup-jre-win.sh && ./mvnw clean package -P nsis`

On **Windows** you must create **target/jre** directory and copy Java files there.
Next run command: `./mvnw clean package -P nsis`

Windows installer executable can be found as `target/setup-WPKG-Cli-(version).exe`

## GNU/Linux:

**Debian/Ubuntu based (deb):**
run command: `./mvnw clean package -P deb`
DEB package can be found in *target* directory.

**RedHat based (rpm):**
run command: `./mvnw clean package -P rpm`
RPM package can be found in *target/rpm/wpkg-cli/RPMS/noarch* directory.

**Arch based (PKGBUILD):**
*PKGBUILD* file can be founded in *package/aur* directory.
