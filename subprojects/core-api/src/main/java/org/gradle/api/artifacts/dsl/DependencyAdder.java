/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.artifacts.dsl;

import org.gradle.api.Action;
import org.gradle.api.Incubating;
import org.gradle.api.NonExtensible;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ExternalModuleDependency;
import org.gradle.api.artifacts.FileCollectionDependency;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.file.FileCollection;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderConvertible;

import javax.annotation.Nullable;

/**
 * <p>A {@code DependencyAdder} is used to add dependencies to a specific configuration.</p>
 *
 * @since 7.6
 */
@Incubating
@NonExtensible
public interface DependencyAdder {
    /* IF YOU UPDATE THIS, ALSO UPDATE THE DSL IN DependencyAdderExtensionModule.java AND DependencyAdderExtensions.kt */

    /**
     * Add a dependency.
     *
     * @param dependencyNotation dependency to add
     * @see DependencyFactory#create(CharSequence) Valid dependency notation for this method
     */
    void add(CharSequence dependencyNotation);

    /**
     * Add a dependency.
     *
     * @param dependencyNotation dependency to add
     * @param configuration an action to configure the dependency
     * @see DependencyFactory#create(CharSequence) Valid dependency notation for this method
     */
    void add(CharSequence dependencyNotation, Action<? super ExternalModuleDependency> configuration);

    /**
     * Add a dependency.
     *
     * @param group the group
     * @param name the name
     * @param version the version
     */
    void add(@Nullable String group, String name, @Nullable String version);

    /**
     * Add a dependency.
     *
     * @param group the group
     * @param name the name
     * @param version the version
     * @param configuration an action to configure the dependency
     */
    void add(@Nullable String group, String name, @Nullable String version, Action<? super ExternalModuleDependency> configuration);

    /**
     * Add a dependency.
     *
     * @param group the group
     * @param name the name
     * @param version the version
     * @param classifier the classifier
     * @param extension the extension
     */
    void add(@Nullable String group, String name, @Nullable String version, @Nullable String classifier, @Nullable String extension);

    /**
     * Add a dependency.
     *
     * @param group the group
     * @param name the name
     * @param version the version
     * @param classifier the classifier
     * @param extension the extension
     * @param configuration an action to configure the dependency
     */
    void add(@Nullable String group, String name, @Nullable String version, @Nullable String classifier, @Nullable String extension, Action<? super ExternalModuleDependency> configuration);

    /**
     * Add a dependency.
     *
     * @param project project to add as a dependency
     */
    void add(Project project);

    /**
     * Add a dependency.
     *
     * @param project project to add as a dependency
     * @param configuration an action to configure the dependency
     */
    void add(Project project, Action<? super ProjectDependency> configuration);

    /**
     * Add a dependency.
     *
     * @param files files to add as a dependency
     */
    void add(FileCollection files);

    /**
     * Add a dependency.
     *
     * @param files files to add as a dependency
     * @param configuration an action to configure the dependency
     */
    void add(FileCollection files, Action<? super FileCollectionDependency> configuration);

    /**
     * Add a dependency.
     *
     * @param externalModule external module to add as a dependency
     */
    void add(ProviderConvertible<? extends MinimalExternalModuleDependency> externalModule);

    /**
     * Add a dependency.
     *
     * @param externalModule external module to add as a dependency
     * @param configuration an action to configure the dependency
     */
    void add(ProviderConvertible<? extends MinimalExternalModuleDependency> externalModule, Action<? super ExternalModuleDependency> configuration);

    /**
     * Add a dependency.
     *
     * @param dependency dependency to add
     */
    void add(Dependency dependency);

    /**
     * Add a dependency.
     *
     * @param dependency dependency to add
     * @param configuration an action to configure the dependency
     */
    <D extends Dependency> void add(D dependency, Action<? super D> configuration);

    /**
     * Add a dependency.
     *
     * @param dependency dependency to add
     */
    void add(Provider<? extends Dependency> dependency);

    /**
     * Add a dependency.
     *
     * @param dependency dependency to add
     * @param configuration an action to configure the dependency
     */
    <D extends Dependency> void add(Provider<? extends D> dependency, Action<? super D> configuration);

    /**
     * Add a bundle.
     *
     * @param bundle the bundle to add
     */
    <D extends Dependency> void bundle(Iterable<? extends D> bundle);

    /**
     * Add a bundle.
     *
     * @param bundle the bundle to add
     * @param configuration an action to configure each dependency in the bundle
     */
    <D extends Dependency> void bundle(Iterable<? extends D> bundle, Action<? super D> configuration);

    /**
     * Add a bundle.
     *
     * @param bundle the bundle to add
     */
    <D extends Dependency> void bundle(Provider<? extends Iterable<? extends D>> bundle);

    /**
     * Add a bundle.
     *
     * @param bundle the bundle to add
     * @param configuration an action to configure each dependency in the bundle
     */
    <D extends Dependency> void bundle(Provider<? extends Iterable<? extends D>> bundle, Action<? super D> configuration);

    /**
     * Add a bundle.
     *
     * @param bundle the bundle to add
     */
    <D extends Dependency> void bundle(ProviderConvertible<? extends Iterable<? extends D>> bundle);

    /**
     * Add a bundle.
     *
     * @param bundle the bundle to add
     * @param configuration an action to configure each dependency in the bundle
     */
    <D extends Dependency> void bundle(ProviderConvertible<? extends Iterable<? extends D>> bundle, Action<? super D> configuration);

}
