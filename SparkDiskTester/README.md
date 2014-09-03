SparkDiskTester
=================

A project that when run, can be used to see how fast the host machine accesses it's storage.
Be warned! The results delivered by this application verge on the meaningless. It's being run in the cloud - every
host machine that it runs on might have different configurations, different storage mediums, different ways of 
accessing that storage, and who knows what noisy neighbours it has in its vicinity sharing the I/O? 

So this application really just of interest only.

It must be run in a project that has access to block storage...

To run it simply use the heat template in the heat subdirectory.
