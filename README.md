# Derealizer

A java serialization library designed for the Virtual Cardboard LWJGL game engine.

Create an enum and easily generate POJO class code.

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

## Usage

1. Copy the following boilerplate code:

```java
import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.*;
import static derealizer.format.SerializationFormat.types;

// Rename 'MySerializationFormats' to whatever you like
public enum MySerializationFormats implements SerializationFormatEnum<SerializationPojo> {

	@FieldNames({ "field1" /*Insert more field names here*/ })
	MY_FORMAT_1(types(INT /*Insert more data types here*/)),
	// Insert more enum values here
	;

	// Do not edit code below this line.

	private final SerializationFormat format;
	private final Class<? extends SerializationPojo> pojoClass;
	private final Class<? extends SerializationPojo> superClass;

	private MySerializationFormats(SerializationFormat format) {
		this.format = format;
		this.pojoClass = null;
		this.superClass = SerializationPojo.class;
	}

	@Override
	public SerializationFormat format() {
		return format;
	}

	@Override
	public Class<? extends SerializationPojo> pojoClass() {
		return pojoClass;
	}

	@Override
	public Class<? extends SerializationPojo> superClass() {
		return superClass;
	}

	public static void main(String[] args) {
		generate(MySerializationFormats.class, SerializationPojo.class);
	}

}
```

2. (Optional) Edit/add your own formats. Add extra serialization data types (LONG, STRING_UTF_8, repeated(), etc.), and
   name your fields using the `@FieldNames` annotation.
3. Run `main()`
4. Copy the console output and paste them into new classes.

Voilà! That's a lot of code you didn't need to write.

## Examples

Your code:

```
@FieldNames({ "weight" })
POTATO(types(INT)),

@FieldNames({ "dogId", "name", "codeName" })
DOG(types(LONG, STRING_UTF8, STRING_UTF8)),

@FieldNames({ "catId", "name", "numLegs", "fearedDogs", "socialSecurityNumber" })
CAT(types(LONG, STRING_UTF8, INT, repeated(pojo(DOG)), optional(LONG))),
```

Generated code:

```java
package networking.protocols;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.*;
import static derealizer.format.SerializationFormat.types;

import derealizer.format.SerializationFormatEnum;
import derealizer.format.SerializationFormat;
import derealizer.format.FieldNames;
import derealizer.format.SerializationPojo;

public enum MySerializationFormats implements SerializationFormatEnum<SerializationPojo> {

	@FieldNames({ "weight" })
	POTATO(types(INT), Potato.class),
	@FieldNames({ "dogId", "name", "codeName" })
	DOG(types(LONG, STRING_UTF8, STRING_UTF8), Dog.class),
	@FieldNames({ "catId", "name", "numLegs", "fearedDogs", "socialSecurityNumber" })
	CAT(types(LONG, STRING_UTF8, INT, repeated(pojo(DOG.class)), optional(LONG)), Cat.class),
	;

	// Do not edit auto-generated code below this line.

	private final SerializationFormat format;
	private final Class<? extends SerializationPojo> pojoClass;
	private final Class<? extends SerializationPojo> superClass;

	private MySerializationFormats(SerializationFormat format, Class<? extends SerializationPojo> pojoClass) {
		this(format, pojoClass, SerializationPojo.class);
	}

	private MySerializationFormats(SerializationFormat format, Class<? extends SerializationPojo> pojoClass, Class<? extends SerializationPojo> superClass) {
		this.format = format;
		this.pojoClass = pojoClass;
		this.superClass = superClass;
	}

	@Override
	public SerializationFormat format() {
		return format;
	}

	@Override
	public Class<? extends SerializationPojo> pojoClass() {
		return pojoClass;
	}

	@Override
	public Class<? extends SerializationPojo> superClass() {
		return superClass;
	}

	public static void main(String[] args) {
		generate(MySerializationFormats.class, SerializationPojo.class);
	}

}
```

```java
package networking.protocols.pojo;

import java.util.List;

public class Potato implements SerializationPojo {

	private int weight;

	public Potato() {
	}

	public Potato(int weight) {
		this.weight = weight;
	}

	@Override
	public void read(SerializationReader reader) {
		this.weight = reader.readInt();
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(weight);
	}

	public int weight() {
		return weight;
	}

}
```

```java
package networking.protocols.pojo;

import java.util.List;

public class Dog implements SerializationPojo {

	private long dogId;
	private String name;
	private String codeName;

	public Dog() {
	}

	public Dog(long dogId, String name, String codeName) {
		this.dogId = dogId;
		this.name = name;
		this.codeName = codeName;
	}

	@Override
	public void read(SerializationReader reader) {
		this.dogId = reader.readLong();
		this.name = reader.readStringUtf8();
		this.codeName = reader.readStringUtf8();
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(dogId);
		writer.consume(name);
		writer.consume(codeName);
	}

	public long dogId() {
		return dogId;
	}

	public String name() {
		return name;
	}

	public String codeName() {
		return codeName;
	}

}
```

```java
package networking.protocols.pojo;

import java.util.List;

public class Cat implements SerializationPojo {

	private long catId;
	private String name;
	private int numLegs;
	private List<Dog> fearedDogs;
	private Long socialSecurityNumber;

	public Cat() {
	}

	public Cat(long catId, String name, int numLegs, List<Dog> fearedDogs, Long socialSecurityNumber) {
		this.catId = catId;
		this.name = name;
		this.numLegs = numLegs;
		this.fearedDogs = fearedDogs;
		this.socialSecurityNumber = socialSecurityNumber;
	}

	@Override
	public void read(SerializationReader reader) {
		this.catId = reader.readLong();
		this.name = reader.readStringUtf8();
		this.numLegs = reader.readInt();
		this.fearedDogs = new ArrayList<>();
		for (byte i0 = 0, numElements0 = reader.readByte(); i0 < numElements0; i0++) {
			Dog pojo1 = new Dog();
			pojo1.read(reader);
			fearedDogs.add(pojo1);
		}
		if (reader.readBoolean()) {
			this.socialSecurityNumber = reader.readLong();
		}
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(catId);
		writer.consume(name);
		writer.consume(numLegs);
		writer.consume((byte) fearedDogs.size());
		for (int i0 = 0; i0 < fearedDogs.size(); i0++) {
			fearedDogs.get(i0).write(writer);
		}
		if (socialSecurityNumber == null) {
			writer.consume(false);
		} else {
			writer.consume(true);
			writer.consume(socialSecurityNumber);
		}
	}

	public long catId() {
		return catId;
	}

	public String name() {
		return name;
	}

	public int numLegs() {
		return numLegs;
	}

	public List<Dog> fearedDogs() {
		return fearedDogs;
	}

	public Long socialSecurityNumber() {
		return socialSecurityNumber;
	}

}
```
