package org.shaechi.jaadas2.entity.sourcesink;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class representing an element on the taint propagation path of a data flow
 * result
 * 
 * @author Steven Arzt
 *
 */
@Entity
@Data
@Table
public class SerializedPathElement extends AbstractSerializedSourceSink {

	public SerializedPathElement(SerializedAccessPath ap, String statement,
			String method) {
		super(ap, statement, method, null);
	}

	public SerializedPathElement() {
		super();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
