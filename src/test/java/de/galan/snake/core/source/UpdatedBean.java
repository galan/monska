package de.galan.snake.core.source;

/** Mock */
public class UpdatedBean {

	public String name;
	public String valueOld;
	public String valueNew;


	public UpdatedBean(String name, String valueNew, String valueOld) {
		this.name = name;
		this.valueNew = valueNew;
		this.valueOld = valueOld;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((valueNew == null) ? 0 : valueNew.hashCode());
		result = prime * result + ((valueOld == null) ? 0 : valueOld.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UpdatedBean other = (UpdatedBean)obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		}
		else if (!name.equals(other.name)) {
			return false;
		}
		if (valueNew == null) {
			if (other.valueNew != null) {
				return false;
			}
		}
		else if (!valueNew.equals(other.valueNew)) {
			return false;
		}
		if (valueOld == null) {
			if (other.valueOld != null) {
				return false;
			}
		}
		else if (!valueOld.equals(other.valueOld)) {
			return false;
		}
		return true;
	}

}
