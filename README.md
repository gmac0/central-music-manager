# Central Music Manager

## What is this thing and why would anyone use it?

This project was born out of a specific problem. There's music on all the devices in my house, in various states of (dis)organization. I have a stereo system set up that I like to listen to music on, but invariably it involved plugging in some laptop or phone and then hoping the tracks I was looking to listen to were actually on that device. I often found myself streaming music off the internet mostly because I didn't want to go through the hassle of locating and hooking up whatever device to play my own tunes. So, I wanted to create a solution. All my music organized in one place. All my music accessible and controllable from any place.

## Specific capabilities

So I set off. Here's what I am hoping the project will do:

* House my entire music collection in a way that is intelligently organized and easily searchable.
* Be accessible from anywhere. Both anywhere in the house and anywhere with internet connectivity so I can play songs from my library even if they aren't currently downloaded on my phone.
* Have playback controls accessible from any internet-enabled device within the home WiFi network. I want to be able to choose a song from my library and play in my living room without getting out of bed.
* Actually sound awesome. Play FLAC files and other losses formats. Also, be able to handle a wide variety of less awesome file types. Both WMA and AAC files hopefully.
* Be lightweight enough to run on a Raspberry Pi or other microcomputer.

## Current state

The first stage of this project is creating a program that could be run on any computer in my house (including Windows and Mac) and transfer music files to an external hard drive. The program also needed to handle duplicate songs, even if they had different file formats or file names. Of course, I also wanted to only keep the highest bitrate version of any one song file. So the MusicOrganizer project was born. You can run the music organizer with `java -jar MusicOrganizer.jar pathToSearch pathToOutput`. MusicOrganizer.jar can be found at out\artifacts\MusicOrganizer_jar. I've run MusicOrganizer successfuly on both Mac and Windows with mp3, flac, m4a, and wma files. This program is still in early state though, so YMMV. Plea let me know if you encounter any bugs!

Once I had my external hard drive filled with music, I plugged it in to a Raspberry Pi running Volumio. There's still work to do to get this backing up somewhere (ideally locally and/or to a cloud service), as well as making the music accessible from outside my WiFi network.

## Links, sources, and similar projects

* Volumio Music Player: https://volumio.org/
  * Solid Raspberry Pi software for playing within a network, but lacking ability to stream tunes to other devices or access music from other locations
* Jaudiotagger library: http://www.jthink.net/jaudiotagger/
  * Java library to fetch audio metatag data
* Python Audio Tools: http://audiotools.sourceforge.net/
  * Possible source of filetype decoders and converters
