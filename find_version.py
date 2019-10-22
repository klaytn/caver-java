#!/usr/local/bin/python
import re

with open("build.gradle", "r") as f:
	l = f.read()
	m = re.search("allprojects\s+{\s+version\s+(.*)\s", l)
	versionString = m.group(1)
	versionString = versionString[1:]
	versionString = versionString[:-1]
	print versionString
