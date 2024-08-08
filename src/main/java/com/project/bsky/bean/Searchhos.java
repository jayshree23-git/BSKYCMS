/**
 * 
 */
package com.project.bsky.bean;

/**
 * @author hrusikesh.mohanty
 *
 */
public class Searchhos {
	public String getPackagecode() {
		return Packagecode;
	}

	public void setPackagecode(String packagecode) {
		Packagecode = packagecode;
	}

	private String packagename;
	private String Packagecode;

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	@Override
	public String toString() {
		return "Searchhos [packagename=" + packagename + ", Packagecode=" + Packagecode + "]";
	}


	

}
