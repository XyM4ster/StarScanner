package org.shaechi.jaadas2.entity.sourcesink;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Information about a flow sink loaded from an external data storage. This
 * object thus cannot reference actual Soot objects
 * 
 * @author Steven Arzt
 *
 */
@Data
@Table
@Entity
public class SerializedSourceInfo extends AbstractSerializedSourceSink {

	@OneToMany(cascade = {CascadeType.ALL})
	private List<SerializedPathElement> propagationPath;
	
	public SerializedSourceInfo(SerializedAccessPath accessPath, String statement,
			String method, String category) {
		this(accessPath, statement, method, category,null);
	}
	
	public SerializedSourceInfo(SerializedAccessPath accessPath, String statement,
			String method, String category, List<SerializedPathElement> propagationPath) {
		super(accessPath, statement, method, category);
		this.propagationPath = propagationPath;
	}

	public SerializedSourceInfo() {

	}

	/**
	 * Adds an element to the propagation path
	 * @param pathElement The path element to add
	 */
	void addPathElement(SerializedPathElement pathElement) {
		if (this.propagationPath == null)
			this.propagationPath = new ArrayList<>();
		this.propagationPath.add(pathElement);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((propagationPath == null) ? 0 : propagationPath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerializedSourceInfo other = (SerializedSourceInfo) obj;
		if (propagationPath == null) {
			if (other.propagationPath != null)
				return false;
		} else if (!propagationPath.equals(other.propagationPath))
			return false;
		return true;
	}
	
	/**
	 * Gets the propagation of this data flow
	 * @return The propagation path of this data flow if one has been loaded,
	 * otherwise null
	 */
	public List<SerializedPathElement> getPropagationPath() {
		return this.propagationPath;
	}

}
