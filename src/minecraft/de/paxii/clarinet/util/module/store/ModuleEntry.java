package de.paxii.clarinet.util.module.store;

import lombok.Data;

/**
 * Created by Lars on 07.02.2016.
 */
@Data
public class ModuleEntry {
	private String module;
	private String description;
	private String version;
	private int build;
}