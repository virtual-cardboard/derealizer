package derealizer;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Queue;

import derealizer.datatype.OptionalDataType;
import derealizer.datatype.PojoDataType;
import derealizer.datatype.RepeatedDataType;
import derealizer.datatype.SerializationDataType;
import derealizer.format.FieldNames;
import derealizer.format.Serializable;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;

public class SerializationClassGenerator {

	public static <T extends Serializable> void generate(Class<? extends SerializationFormatEnum> formatEnum, Class<T> pojoBaseClass) {
		System.out.println(separator("Enum class"));
		System.out.println(generateEnumClass(formatEnum, pojoBaseClass));
		System.out.println(separator());
		System.out.println();
		for (SerializationFormatEnum format : formatEnum.getEnumConstants()) {
			Enum<?> e = (Enum<?>) format;
			verifyEnumLabels(format);
			System.out.println(separator(toCamelCase(e.name())));
			System.out.println(generatePOJOClass(format, pojoBaseClass));
			System.out.println(separator());
		}
	}

	private static <T extends Serializable> String generateEnumClass(Class<? extends SerializationFormatEnum> formatEnum, Class<T> pojoBaseClass) {
		String s = "";
		s += formatEnum.getPackage() + ";\n";
		s += "\n";
		s += "import static " + SerializationClassGenerator.class.getCanonicalName() + ".generate;\n";
		s += "import static " + SerializationDataType.class.getCanonicalName() + ".BOOLEAN;\n";
		s += "import static " + SerializationDataType.class.getCanonicalName() + ".BYTE;\n";
		s += "import static " + SerializationDataType.class.getCanonicalName() + ".LONG;\n";
		s += "import static " + SerializationDataType.class.getCanonicalName() + ".INT;\n";
		s += "import static " + SerializationDataType.class.getCanonicalName() + ".SHORT;\n";
		s += "import static " + SerializationDataType.class.getCanonicalName() + ".STRING_UTF8;\n";
		s += "import static " + SerializationDataType.class.getCanonicalName() + ".optional;\n";
		s += "import static " + SerializationDataType.class.getCanonicalName() + ".pojo;\n";
		s += "import static " + SerializationDataType.class.getCanonicalName() + ".repeated;\n";
		s += "import static " + SerializationFormat.class.getCanonicalName() + ".types;\n";
		s += "\n";
		s += "import " + SerializationFormatEnum.class.getCanonicalName() + ";\n";
		s += "import " + SerializationFormat.class.getCanonicalName() + ";\n";
		s += "import " + FieldNames.class.getCanonicalName() + ";\n";
		s += "import " + Serializable.class.getCanonicalName() + ";\n";
		s += "\n";
		s += "public enum " + formatEnum.getSimpleName() + " implements " + SerializationFormatEnum.class.getSimpleName() + " {\n";
		s += "\n";
		for (SerializationFormatEnum format : formatEnum.getEnumConstants()) {
			Enum<?> e = (Enum<?>) format;
			Queue<SerializationDataType> dataTypes = format.format().dataTypes();
			s += "	@" + FieldNames.class.getSimpleName() + "({ " + commaify(quotify(getFieldNames(format))) + " })\n";
			s += "	" + e.name() + "(types(" + commaify(dataTypes) + "), " + toCamelCase(e.name()) + ".class),\n";
		}
		s += "	;\n";
		s += "\n";
		s += "	private final " + SerializationFormat.class.getSimpleName() + " format;\n";
		s += "	private final Class<? extends " + pojoBaseClass.getSimpleName() + "> pojoClass;\n";
		s += "\n";
		s += "	private " + formatEnum.getSimpleName() + "(" + SerializationFormat.class.getSimpleName() + " format, Class<? extends " + pojoBaseClass.getSimpleName() + "> pojoClass) {\n";
		s += "		this.format = format;\n";
		s += "		this.pojoClass = pojoClass;\n";
		s += "	}\n";
		s += "\n";
		s += "	@Override\n";
		s += "	public " + SerializationFormat.class.getSimpleName() + " format() {\n";
		s += "		return format;\n";
		s += "	}\n";
		s += "\n";
		s += "	@Override\n";
		s += "	public Class<? extends " + pojoBaseClass.getSimpleName() + "> pojoClass() {\n";
		s += "		return pojoClass;\n";
		s += "	}\n";
		s += "\n";
		s += "	public static void main(String[] args) {\n";
		s += "		generate(" + formatEnum.getSimpleName() + ".class, " + pojoBaseClass.getSimpleName() + ".class);\n";
		s += "	}\n";
		s += "\n";
		s += "}\n";
		return s;
	}

	private static <T extends Serializable> String generatePOJOClass(SerializationFormatEnum formatEnum, Class<T> pojoBaseClass) {
		Enum<?> e = (Enum<?>) formatEnum;
		String s = "";
		s += formatEnum.getClass().getPackage() + ".pojo;\n\n";
		s += "import static " + formatEnum.getClass().getName() + "." + e.name() + ";\n\n";
		s += "import java.util.List;\n\n";
		if (pojoBaseClass.equals(Serializable.class)) {
			s += "public class " + toCamelCase(e.name()) + " implements " + Serializable.class.getSimpleName() + "<" + formatEnum.getClass().getSimpleName() + "> {\n";
		} else {
			String implementsOrExtends = pojoBaseClass.isInterface() ? "implements" : "extends";
			s += "public class " + toCamelCase(e.name()) + " " + implementsOrExtends + " " + pojoBaseClass.getSimpleName() + " {\n";
		}
		s += "\n";
		String[] fieldNames = getFieldNames(formatEnum);
		String[] fieldTypes = formatEnum.format().dataTypes().stream().map(SerializationClassGenerator::toFieldType).toArray(String[]::new);
		for (int i = 0; i < fieldNames.length; i++) {
			s += "	private " + fieldTypes[i] + " " + fieldNames[i] + ";\n";
		}
		s += "\n";
		// No-arg constructor
		s += "	public " + toCamelCase(e.name()) + "() {\n";
		s += "	}\n\n";
		// Constructor
		if (fieldNames.length > 0) {
			s += "	public " + toCamelCase(e.name()) + "(";
			for (int i = 0; i < fieldNames.length - 1; i++) {
				s += fieldTypes[i] + " " + fieldNames[i] + ", ";
			}
			s += fieldTypes[fieldTypes.length - 1] + " " + fieldNames[fieldNames.length - 1];
			s += ") {\n";
			for (int i = 0; i < fieldNames.length; i++) {
				s += "		this." + fieldNames[i] + " = " + fieldNames[i] + ";\n";
			}
			s += "	}\n";
		}
		// byte[] Constructor
		s += "\n";
		s += "	public " + toCamelCase(e.name()) + "(byte[] bytes) {\n";
		s += "		read(new " + SerializationReader.class.getSimpleName() + "(bytes));\n";
		s += "	}\n\n";
		// Format enum getter
		s += "	@Override\n";
		s += "	public " + formatEnum.getClass().getSimpleName() + " formatEnum() {\n";
		s += "		return " + e.name() + ";\n";
		s += "	}\n\n";
		// Read
		s += "	@Override\n";
		s += "	public void read(" + SerializationReader.class.getSimpleName() + " reader) {\n";
		// Call super.read if needed
		if (isImplemented(pojoBaseClass, "read", SerializationReader.class)) {
			s += "		super.read(reader);\n";
		}
		SerializationDataType[] dataTypes = formatEnum.format().dataTypes().stream().toArray(SerializationDataType[]::new);
		for (int i = 0; i < fieldNames.length; i++) {
			SerializationDataType dataType = dataTypes[i];
			s += toReadMethod(fieldNames[i], dataType);
		}
		s += "	}\n\n";
		// Write
		s += "	@Override\n";
		s += "	public void write(SerializationWriter writer) {\n";
		// Call super.write if needed
		if (isImplemented(pojoBaseClass, "write", SerializationWriter.class)) {
			s += "		super.write(writer);\n";
		}
		for (int i = 0; i < fieldNames.length; i++) {
			s += toWriteMethod(fieldNames[i], dataTypes[i]);
		}
		s += "	}\n\n";
		// Getters
		for (int i = 0; i < fieldNames.length; i++) {
			s += "	public " + fieldTypes[i] + " " + fieldNames[i] + "() {\n";
			s += "		return " + fieldNames[i] + ";\n";
			s += "	}\n";
			s += "\n";
		}
		s += "}\n";
		return s;
	}

	private static String toReadMethod(String fieldName, SerializationDataType dataType) {
		return toReadMethod(fieldName, dataType, 0, 0, null);
	}

	private static String toReadMethod(String variableName, SerializationDataType dataType, int numIndents, int variablesCount, String listName) {
		String indents = "\t\t";
		for (int i = 0; i < numIndents; i++) {
			indents += "\t";
		}
		if (listName == null) {
			// Set a field
			switch (dataType.type) {
				case LONG:
				case INT:
				case SHORT:
				case BYTE:
				case BOOLEAN:
				case DOUBLE:
				case FLOAT:
				case STRING_UTF8:
					return indents + "this." + variableName + " = reader.read" + toCamelCase(dataType.type.name().toLowerCase()) + "();\n";
				case REPEATED: {
					SerializationDataType repeatedDataType = ((RepeatedDataType) dataType).repeatedDataType;
					String iterVariable = "i" + variablesCount;
					String numVariable = "numElements" + variablesCount;
					String s = indents + "this." + variableName + " = new ArrayList<>();\n";
					s += indents + "for (byte " + iterVariable + " = 0, " + numVariable + " = reader.readByte(); " + iterVariable + " < " + numVariable + "; " + iterVariable + "++) {\n";
					numIndents++;
					variablesCount++;
					s += toReadMethod(null, repeatedDataType, numIndents, variablesCount, variableName);
					s += indents + "}\n";
					return s;
				}
				case OPTIONAL:
					SerializationDataType optionalDataType = ((OptionalDataType) dataType).optionalDataType;
					return indents + "if (reader.readBoolean()) {\n" +
							"	" + toReadMethod(variableName, optionalDataType, numIndents, variablesCount, null) +
							indents + "}\n";
				case POJO: {
					SerializationFormatEnum pojoFormatEnum = ((PojoDataType) dataType).pojoFormatEnum();
					String pojoVariableType = toCamelCase(pojoFormatEnum.toString());
					return indents + "this." + variableName + " = new " + pojoVariableType + "();\n" +
							indents + "this." + variableName + ".read(reader);\n";
				}
				default:
					throw new RuntimeException("Unhandled SerializationDataType: " + dataType.type + "\nCould not interpret data type as a field type.");
			}
		} else {
			// Add something to a list
			switch (dataType.type) {
				case LONG:
				case INT:
				case SHORT:
				case BYTE:
				case BOOLEAN:
				case STRING_UTF8:
					return indents + listName + ".add(reader.read" + toCamelCase(dataType.type.name().toLowerCase()) + "());\n";
				case REPEATED:
					SerializationDataType repeatedDataType = ((RepeatedDataType) dataType).repeatedDataType;
					String iterVariable = "i" + variablesCount;
					String numVariable = "numElements" + variablesCount;
					String newListName = "list" + variablesCount;
					return indents + "List<" + convertPrimitiveToWrapper(repeatedDataType) + "> " + newListName + " = new ArrayList<>();\n" +
							indents + "for (byte " + iterVariable + " = 0, " + numVariable + " = reader.readByte(); " + iterVariable + " < " + numVariable + "; " + iterVariable + "++) {\n" +
							toReadMethod(null, repeatedDataType, ++numIndents, ++variablesCount, variableName) +
							indents + "}\n" +
							indents + "this." + variableName + ".add(" + newListName + ");\n";
//			    case ONE_OF:
				case OPTIONAL:
					SerializationDataType optionalDataType = ((OptionalDataType) dataType).optionalDataType;
					return indents + "if (reader.readBoolean()) {\n" +
							"	" + toReadMethod(variableName, optionalDataType, numIndents, variablesCount, listName) +
							indents + "} else {\n" +
							indents + "\t" + listName + ".add(null);\n" +
							indents + "}\n";
				case POJO: {
					SerializationFormatEnum pojoFormatEnum = ((PojoDataType) dataType).pojoFormatEnum();
					String pojoVariableType = toCamelCase(pojoFormatEnum.toString());
					String pojoVariableName = "pojo" + variablesCount;
					return indents + pojoVariableType + " " + pojoVariableName + " = new " + pojoVariableType + "();\n" +
							indents + pojoVariableName + ".read(reader);\n" +
							indents + listName + ".add(" + pojoVariableName + ");\n";
				}

				default:
					throw new RuntimeException("Unhandled SerializationDataType: " + dataType.type + "\nCould not add data type to " + listName);
			}
		}
	}

	private static String toWriteMethod(String variableName, SerializationDataType dataType) {
		return toWriteMethod(variableName, dataType, 0, 0);
	}

	private static String toWriteMethod(String variableName, SerializationDataType dataType, int numIndents, int variablesCount) {
		String indents = "\t\t";
		for (int i = 0; i < numIndents; i++) {
			indents += "\t";
		}
		// Set a field
		switch (dataType.type) {
			case LONG:
			case INT:
			case SHORT:
			case BYTE:
			case BOOLEAN:
			case DOUBLE:
			case FLOAT:
			case STRING_UTF8:
				return indents + "writer.consume(" + variableName + ");\n";
			case REPEATED:
				SerializationDataType repeatedDataType = ((RepeatedDataType) dataType).repeatedDataType;
				String iterVariable = "i" + variablesCount;
				String s = indents + "writer.consume((byte) " + variableName + ".size());\n" +
						indents + "for (int " + iterVariable + " = 0; " + iterVariable + " < " + variableName + ".size(); " + iterVariable + "++) {\n";
				s += toWriteMethod(variableName + ".get(" + iterVariable + ")", repeatedDataType, ++numIndents, ++variablesCount);
				s += indents + "}\n";
				return s;
			case OPTIONAL:
				SerializationDataType optionalDataType = ((OptionalDataType) dataType).optionalDataType;
				return indents + "if (" + variableName + " == null) {\n" +
						indents + "	writer.consume(false);\n" +
						indents + "} else {\n" +
						indents + "	writer.consume(true);\n" +
						toWriteMethod(variableName, optionalDataType, numIndents + 1, variablesCount) +
						indents + "}\n";
			case POJO:
				return indents + variableName + ".write(writer);\n";
			default:
				throw new RuntimeException("Unhandled SerializationDataType: " + dataType.type + "\nCould not interpret data type as a field type.");
		}
	}

	private static void verifyEnumLabels(SerializationFormatEnum format) {
		if (getFieldNames(format).length != format.format().dataTypes().size()) {
			Enum<?> e = (Enum<?>) format;
			throw new InputMismatchException("Serialization format definition " + e.name() + " needs the same number of labels in the @" + FieldNames.class.getSimpleName() +
					" annotation " + "as data types in the format for the class generator to create a POJO class.\n" +
					"For example:\n	@" + FieldNames.class.getSimpleName() + "({\"field1\", \"field2\"})\n	FORMAT_1(format().with(INT, INT))");
		}
	}

	private static String toFieldType(SerializationDataType dataType) {
		switch (dataType.type) {
			case LONG:
			case INT:
			case SHORT:
			case BYTE:
			case BOOLEAN:
			case DOUBLE:
			case FLOAT:
				return dataType.type.name().toLowerCase();
			case STRING_UTF8:
				return "String";
			case ONE_OF:
				// TODO figure out how to concisely express a "one of ..." data type
				return "Object";
			case REPEATED:
				return "List<" + convertPrimitiveToWrapper(((RepeatedDataType) dataType).repeatedDataType) + ">";
			case POJO:
				return toCamelCase(((PojoDataType) dataType).pojoFormatEnum().toString());
			case OPTIONAL:
				return convertPrimitiveToWrapper(((OptionalDataType) dataType).optionalDataType);
			default:
				throw new RuntimeException("Unhandled SerializationDataType: " + dataType.type + "\nCould not interpret data type as a field type.");
		}
	}

	private static String convertPrimitiveToWrapper(SerializationDataType dataType) {
		switch (dataType.type) {
			case INT:
				return "Integer";
			case LONG:
			case SHORT:
			case BYTE:
			case BOOLEAN:
			case DOUBLE:
			case FLOAT:
				String n = dataType.type.name();
				return n.charAt(0) + n.substring(1).toLowerCase();
			default:
				return toFieldType(dataType);
		}
	}

	private static String[] getFieldNames(SerializationFormatEnum formatEnum) {
		String name = ((Enum<?>) formatEnum).name();
		try {
			Field field = formatEnum.getClass().getField(name);
			if (!field.isAnnotationPresent(FieldNames.class)) {
				throw new RuntimeException("Serialization format definition " + name + " is missing an @" + FieldNames.class.getSimpleName() + " annotation.");
			}
			return field.getAnnotation(FieldNames.class).value();
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("Serialization format definition " + name + " needs field names for the class generator to create a POJO class.\n" +
					"Add a @" + FieldNames.class.getSimpleName() + " annotation to define field names. For example:\n	@" + FieldNames.class.getSimpleName() + "({\"field1\", \"field2\"})");
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	private static String dataTypeToString(SerializationDataType dataType) {
		switch (dataType.type) {
			case OPTIONAL:
				OptionalDataType optionalDataType = (OptionalDataType) dataType;
				return "optional(" + dataTypeToString(optionalDataType.optionalDataType) + ")";
			case POJO:
				PojoDataType pojoDataType = (PojoDataType) dataType;
				SerializationFormatEnum pojoFormatEnum = pojoDataType.pojoFormatEnum();
				return "pojo(" + pojoFormatEnum.toString() + ")";
			case REPEATED:
				RepeatedDataType repeatedDataType = (RepeatedDataType) dataType;
				return "repeated(" + dataTypeToString(repeatedDataType.repeatedDataType) + ")";
			default:
				return dataType.type.name();
		}
	}

	private static String[] quotify(String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			strings[i] = "\"" + strings[i] + "\"";
		}
		return strings;
	}

	private static String commaify(String[] strings) {
		return commaify(asList(strings));
	}

	private static String commaify(Queue<SerializationDataType> dataTypes) {
		List<String> strings = dataTypes.stream().map(SerializationClassGenerator::dataTypeToString).collect(toList());
		return commaify(strings);
	}

	private static String commaify(Iterable<String> strings) {
		String s = "";
		for (String string : strings) {
			s += string + ", ";
		}
		if (s.length() == 0) {
			return "";
		}
		return s.substring(0, s.length() - 2);

	}

	private static String toCamelCase(String s) {
		String[] parts = s.split("_");
		String camelCaseString = "";
		for (String part : parts) {
			camelCaseString = camelCaseString + toProperCase(part);
		}
		return camelCaseString;
	}

	private static String toProperCase(String s) {
		if (s.isEmpty()) {
			return s;
		}
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	private static String separator() {
		return separator("");
	}

	private static String separator(String text) {
		String s = "";
		int half = Math.max(0, (90 - text.length()) / 2);
		for (int i = 0; i < half; i++) {
			s += "=";
		}
		s += text;
		for (int i = 0; i < 90 - half - text.length(); i++) {
			s += "=";
		}
		return s;
	}

	private static <T extends Serializable> boolean isImplemented(Class<T> pojoBaseClass, String methodName, Class<?> param) {
		try {
			return !isAbstract(pojoBaseClass.getMethod(methodName, param).getModifiers());
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

}
