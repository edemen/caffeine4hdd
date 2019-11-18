# caffeine4hdd
Keeps an HDD awake by periodically writing random content into a temp file on a provided path

## Usecase
I am running a third-party application that lazy-loads some resources from disk. If the disk was suspended by the OS to preserve power, the call from the application will wake it up and read the resource, but while the drive is waking up, the application will be unresponsive.

By keeping the drive awake, I can avoid these periods of application being unresponsive.