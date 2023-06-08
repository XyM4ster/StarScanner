package org.shaechi.jaadas2.entity.sourcesink;

import lombok.Data;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing an access path read from an external storage. Therefore,
 * this class cannot reference any Soot objects.
 * 
 * @author Steven Arzt
 *
 */
@Entity
@Data
@Table
public class SerializedAccessPath {
	
	private String base;
	private String baseType;
	private boolean taintSubFields;

	@ElementCollection
	private List<String> fields;
	@ElementCollection
	private List<String> types;

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Creates a new instance of the SerializedAccessPath class
	 * @param base The base variable
	 * @param baseType The type of the base variable
	 * @param taintSubFields Specifies whether fields following the specified
	 * ones shall also be considered as tainted
	 * @param fields The sequence of fields
	 * @param types The types of the fields
	 */
	public SerializedAccessPath(String base, String baseType, boolean taintSubFields,
			List<String> fields, List<String> types) {
		this.base = base;
		this.baseType = baseType;
		this.taintSubFields = taintSubFields;
		this.fields = fields;
		this.types = types;
	}

	public SerializedAccessPath() {

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result
				+ ((baseType == null) ? 0 : baseType.hashCode());
		result = prime * result + Arrays.hashCode(fields.toArray(new String[0]));
		result = prime * result + (taintSubFields ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(types.toArray(new String[0]));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerializedAccessPath other = (SerializedAccessPath) obj;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
		if (baseType == null) {
			if (other.baseType != null)
				return false;
		} else if (!baseType.equals(other.baseType))
			return false;
		if (!Arrays.equals(this.getFieldsArray(), other.getFieldsArray()))
			return false;
		if (taintSubFields != other.taintSubFields)
			return false;
		if (!Arrays.equals(this.getTypesArray(), other.getTypesArray()))
			return false;
		return true;
	}
	
	/**
	 * Gets the tainted base object
	 * @return The tainted base object
	 */
	public String getBase() {
		return this.base;
	}
	
	/**
	 * Gets the type of the tainted base object
	 * @return The type of the tainted base object
	 */
	public String getBaseType() {
		return this.baseType;
	}
	
	/**
	 * Gets whether fields following the specified ones shall also be
	 * considered as tainted
	 * @return True if sub-fields shall be considered as tainted, otherwise
	 * false
	 */
	public boolean getTaintSubFields() {
		return this.taintSubFields;
	}
	
	/**
	 * Gets the sequence of fields in this access path
	 * @return The sequence of fields in this access path
	 */
	public String[] getFieldsArray() {
		return this.fields.toArray(new String[0]);
	}
	
	/**
	 * Gets the types of the fields in this access path
	 * @return The types of the fields in this access path
	 */
	public String[] getTypesArray() {
		return this.types.toArray(new String[0]);
	}

	@Override
	public String toString() {
		return base +
				": " + String.join(" ,",fields);
	}
}
