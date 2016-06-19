package de.paxii.clarinet.util.module.settings;

import com.google.gson.*;
import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.client.PostLoadModulesEvent;
import de.paxii.clarinet.event.events.game.StopGameEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.settings.ClientSettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map.Entry;

public class ModuleSettingsHandler {
	public ModuleSettingsHandler() {
		Wrapper.getEventManager().register(this);
	}

	@EventHandler
	public void onPostLoadModules(PostLoadModulesEvent event) {
		new Thread(() -> {
			try {
				Gson gson = new Gson();

				File settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/modules.json");

				if (settingsFile.exists()) {
					BufferedReader br = new BufferedReader(new FileReader(settingsFile));
					String file = "", line;

					while ((line = br.readLine()) != null) {
						file += line;
					}

					br.close();

					if (file.length() > 0) {
						ModuleSettingsContainer moduleSettingsContainer = gson.fromJson(file, ModuleSettingsContainer.class);

						moduleSettingsContainer.getModuleSettings().forEach(moduleSettings -> {
							if (Wrapper.getModuleManager().doesModuleExist(moduleSettings.getModuleName())) {
								Module module = Wrapper.getModuleManager().getModule(moduleSettings.getModuleName());

								module.setModuleSettings(moduleSettings.getModuleSettings());

								/**
								 * FIXME Find some other way to handle this. Maybe using a JsonDeserializer?
								 * This converts settings stored as doubles to int if the value stays the same
								 * Gson uses Doubles rather than Integers when guessing data types.
								 */
								moduleSettings.getModuleSettings().forEach((key, value) -> {
									if (value.getValue() instanceof Double) {
										double doubleValue = (double) value.getValue();

										int intValue = (int) ((long) doubleValue);
										if (doubleValue == intValue) {
											value.setValue(intValue);
										}
									}
								});

								moduleSettings.getModuleValues().forEach((k, v) -> {
									if (ValueBase.doesValueExist(v.getName())) {
										ValueBase vb = ValueBase.getValueBase(v.getName());

										if (vb != null) {
											vb.setValue(v.getValue());
											vb.setMin(v.getMin());
											vb.setMax(v.getMax());
											vb.setName(v.getName());
										}
									}
								});

								module.setKey(moduleSettings.getModuleKey());
								module.onStartup();
							}
						});
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@EventHandler
	public void onStopGame(StopGameEvent event) {
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			File settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/modules.json");

			settingsFile.delete();

			if (settingsFile.createNewFile()) {
				ModuleSettingsContainer moduleSettingsContainer = new ModuleSettingsContainer(new ArrayList<>());

				for (Entry<String, Module> moduleEntry : Wrapper.getModuleManager().getModuleList().entrySet()) {
					Module module = moduleEntry.getValue();

					module.onShutdown();

					moduleSettingsContainer.getModuleSettings().add(new ModuleSettingsObject(module));
				}

				String jsonString = gson.toJson(moduleSettingsContainer);

				FileWriter fileWriter = new FileWriter(settingsFile);
				fileWriter.write(jsonString);
				fileWriter.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Wrapper.getEventManager().unregister(this);
	}
}
