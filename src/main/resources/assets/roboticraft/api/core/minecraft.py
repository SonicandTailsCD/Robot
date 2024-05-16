from __future__ import absolute_import
from .connection import Connection
from .block import Block
import math
import atexit
from os import environ

""" Minecraft api v0.1_2

    Note: many methods have the parameter *arg. This solution makes it
    simple to allow different types, and variable number of arguments.
    The actual magic is a mix of flatten_parameters() and __iter__. Example:
    A Cube class could implement __iter__ to work in Minecraft.setBlocks(c, id).

    (Because of this, it's possible to "erase" arguments. CmdPlayer removes
     entityId, by injecting [] that flattens to nothing)

    @author: Aron Nieminen, Mojang AB
    @author: Dom Amato"""

def fixPipe(s):
    return s.replace('&#124;', '|').replace('&amp;','&')

def stringToBlockWithNBT(s, pipeFix = False):
    data = s.split(",")
    id = int(data[0])
    if len(data) <= 1:
        return Block(id)
    elif len(data) <= 2:
        return Block(id,int(data[1]))
    else:
        nbt = ','.join(data[2:])
        if pipeFix:
            nbt = fixPipe(nbt)
        return Block(id,int(data[1]),nbt)

class Minecraft:
    """The main class to interact with a running instance of Minecraft Pi."""

    def __init__(self, connection=None):
        if connection:
            self.conn = connection
        else:
            self.conn = Connection()


        
        self.playerId = None
        
        try:
             self.playerId = int(environ['MINECRAFT_PLAYER_ID'])
        except:
            raise RuntimeError("Could not get player ID")
        
        self.enabledNBT = False
        atexit.register(self.conn.close, self.playerId)

    def __del__(self):
        try:
            atexit.unregister(self.conn.close)
        except:
            pass 

    @staticmethod
    def create(address = None, port = None):
        return Minecraft(Connection(address, port))
        
if __name__ == "__main__":
    mc = Minecraft.create()
    mc.postToChat("Hello, Minecraft!")
