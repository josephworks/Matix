package de.paxii.clarinet.module;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.EventManager;
import de.paxii.clarinet.event.events.client.PostLoadModulesEvent;
import de.paxii.clarinet.event.events.game.KeyPressedEvent;
import de.paxii.clarinet.module.external.ExternalModuleLoader;
import de.paxii.clarinet.util.module.store.ModuleStore;
import de.paxii.clarinet.util.settings.ClientSettings;
import lombok.Getter;
import org.reflections.Reflections;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleManager {
	@Getter
	private final ConcurrentHashMap<String, Module> moduleList = new ConcurrentHashMap<>();
	@Getter
	private final ConcurrentHashMap<String, Integer> keybindList = new ConcurrentHashMap<>();
	@Getter
	private ExternalModuleLoader externalModuleLoader;

	public ModuleManager() {
		this.externalModuleLoader = new ExternalModuleLoader();
		Wrapper.getEventManager().register(this);
		new Thread(this::addModules).start();
	}

	public void reloadModules() {
		addModules();
	}

	private void addModules() {
		for (ModuleCategory m : ModuleCategory.values()) {
			String packageName = "de.paxii.clarinet.module." + m.toString().toLowerCase();
			System.out.println("Searching for Modules in " + packageName);

			Reflections reflections = new Reflections(packageName);

			Set<Class<? extends Module>> allClasses =
					reflections.getSubTypesOf(Module.class);

			for (Class<? extends Module> c : allClasses) {
				try {
					Module newModule = c.newInstance();
					this.addModule(newModule);
				} catch (Exception x) {
					x.printStackTrace();
				}
			}
		}

		this.externalModuleLoader.loadModules(() -> EventManager.call(new PostLoadModulesEvent()));
	}

	public void addModule(Module module) {
		for (String blocked : ModuleStore.getModulesToDelete()) {
			if (module.getName().equals(blocked))
				return;
		}

		if (!doesModuleExist(module.getName())) {
			System.out.println("Loading " + module.getName() + "...");
			this.moduleList.put(module.getName(), module);
		}
	}

	public void removeModule(Module module) {
		if (doesModuleExist(module.getName())) {
			System.out.println("Removing " + module.getName() + "...");
			this.moduleList.remove(module.getName());
		}
	}

	public boolean doesModuleExist(String moduleName) {
		for (Entry<String, Module> command : moduleList.entrySet()) {
			if (command.getKey().equalsIgnoreCase(moduleName)) {
				return true;
			}
		}

		return false;
	}

	public Module getModule(String moduleName) {
		if (doesModuleExist(moduleName)) {
			return this.moduleList.get(moduleName);
		} else {
			return null;
		}
	}

	public Module getModuleIgnoreCase(String moduleName) {
		for (Entry<String, Module> command : moduleList.entrySet()) {
			if (command.getKey().equalsIgnoreCase(moduleName)) {
				return command.getValue();
			}
		}

		return null;
	}

	public ConcurrentHashMap<String, Module> getModulesByCategory(ModuleCategory moduleCategory) {
		ConcurrentHashMap<String, Module> modules = new ConcurrentHashMap<>();

		this.moduleList.forEach((k, m) -> {
			if (m.getCategory() == moduleCategory) {
				modules.put(k, m);
			}
		});

		return modules;
	}

	public boolean isModuleActive(String moduleName) {
		Module module = this.getModule(moduleName);
		return module != null && module.isEnabled();
	}

	@EventHandler
	private void handleKeyPress(KeyPressedEvent event) {
		if (!ClientSettings.getValue("client.hidden", Boolean.class)) {
			for (Entry<String, Integer> entry : this.keybindList.entrySet()) {
				String moduleName = entry.getKey();
				int moduleKey = entry.getValue();

				if (moduleKey == event.getKey()) {
					Module module = getModule(moduleName);

					if (module != null) {
						module.toggle();
					}
				}
			}
		}
	}

	public void addKey(String moduleName, int key) {
		if (this.keybindList.containsKey(moduleName)) {
			this.removeKey(moduleName);
		}

		this.keybindList.put(moduleName, key);
	}

	public void removeKey(String moduleName) {
		this.keybindList.remove(moduleName);
	}
}
