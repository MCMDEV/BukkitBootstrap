/*
 * Copyright 2020 MCMDEV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.mcmdev.spigotmixinbootstrap.mod;

import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipFile;

public class LaunchModLoader {

    private final Path path;

    private int modCount;
    private int mixinCount;

    public LaunchModLoader(Path path) {
        this.path = path;
    }

    public void loadAll(LaunchClassLoader launchClassLoader)   {
        File file = path.toFile();
        
        if(!file.exists())  {
            file.mkdirs();
        }

        for (File listFile : file.listFiles()) {
            if(!listFile.getName().endsWith(".jar"))    {
                continue;
            }

            modCount++;

            try {
                launchClassLoader.addURL(listFile.toURI().toURL());

                ZipFile zipFile = new ZipFile(listFile);
                zipFile.stream().forEach(zipEntry -> {
                    if(zipEntry.getName().startsWith("mixins") && zipEntry.getName().endsWith(".json"))  {
                        Mixins.addConfiguration(zipEntry.getName());
                        mixinCount++;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        System.out.println("[SpigotMixinBootstrap] Loaded " + modCount + " mods and " + mixinCount + " mixins.");
    }
}
