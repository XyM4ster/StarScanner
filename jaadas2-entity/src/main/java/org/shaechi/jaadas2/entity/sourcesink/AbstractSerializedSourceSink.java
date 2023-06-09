package org.shaechi.jaadas2.entity.sourcesink;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Abstract base class for serialized source or sink information
 * 
 * @author Steven Arzt
 *
 */
@Data
@MappedSuperclass
public class AbstractSerializedSourceSink implements Serializable {
	public AbstractSerializedSourceSink() {
	}

	@OneToOne(cascade = {CascadeType.ALL})
	private SerializedAccessPath accessPath;

	@Column(length = 100000)
	private String statement;
	@Column(length = 100000)
	private String method;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;

	public String getCategory() {
		return category;
	}

	private String category;
	
	/**
	 * Creates a new instance of the AbstractSerializedSourceSink class
	 * @param ap The tainted access path at this source or sink
	 * @param statement The statement that represents this source or sink
	 * @param method The method containing the source or sink statement
	 */
	protected AbstractSerializedSourceSink(SerializedAccessPath ap,
			String statement, String method, String category) {
		this.accessPath = ap;
		this.statement = statement;
		this.method = method;
		this.category = category;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accessPath == null) ? 0 : accessPath.hashCode());
		result = prime * result
				+ ((statement == null) ? 0 : statement.hashCode());
		result = prime * result
				+ ((method == null) ? 0 : method.hashCode());
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
		AbstractSerializedSourceSink other = (AbstractSerializedSourceSink) obj;
		if (accessPath == null) {
			if (other.accessPath != null)
				return false;
		} else if (!accessPath.equals(other.accessPath))
			return false;
		if (statement == null) {
			if (other.statement != null)
				return false;
		} else if (!statement.equals(other.statement))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}
	
	/**
	 * Gets the tainted access path at the current source or sink
	 * @return The tainted access path at the current source or sink
	 */
	public SerializedAccessPath getAccessPath() {
		return this.accessPath;
	}
	
	/**
	 * Gets the statement representing this source or sink
	 * @return The statement representing this source or sink
	 */
	public String getStatement() {
		return this.statement;
	}
	
	/**
	 * Gets the method containing the source or sink statement
	 * @return The method containing the source or sink statement
	 */
	public String getMethod() {
		return this.method;
	}

}
