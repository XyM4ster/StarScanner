package org.shaechi.jaadas2.entity.sourcesink;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Information about a flow sink loaded from an external data storage. This
 * object thus cannot reference actual Soot objects
 * 
 * @author Steven Arzt
 *
 */
@Entity
@Data
@Table
public class SerializedSinkInfo extends AbstractSerializedSourceSink {

	public SerializedSinkInfo(SerializedAccessPath accessPath, String statement,
			String method, String category) {
		super(accessPath, statement, method, category);
	}


	public SerializedSinkInfo() {

	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
