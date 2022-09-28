# Derealizer

A java serialization library designed for the Virtual Cardboard LWJGL game engine.

The generated POJO classes can serialize and deserialize with the **fewest number of bytes** possible. The
auto-generated serialization code serializes and deserializes using the fewest number of bytes possible

## Features

| Feature       | Description                                                                    |
|---------------|--------------------------------------------------------------------------------|
| Ease of use   | It's simple to create serialization formats and generate new POJO classes.     |
| Serialization | _Derealizer_ is great at serializing java objects into byte arrays.            |
| Efficiency    | _Derealizer_'s byte arrays _might_ be more compact than Google's protobuffers. |

In our game engine, _Derealizer_ is used to convert network events into byte arrays so that they can be sent through the
network.
