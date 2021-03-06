/*
 * Copyright 2013 Rimero Solutions
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
package com.rimerosolutions.ant.git.tasks;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.rimerosolutions.ant.git.AbstractGitRepoAwareTask;
import com.rimerosolutions.ant.git.GitBuildException;
import com.rimerosolutions.ant.git.GitSettings;
import com.rimerosolutions.ant.git.GitTaskUtils;
import com.rimerosolutions.ant.git.MissingRequiredGitSettingsException;

/**
 * Create a git tag.
 *
 * <pre>{@code
 * <git:git directory="${testLocalRepo}" verbose="true">
 * <git:tag name="${dummy.tag.name}"/>
 * </git:git>}</pre>
 *
 * <p><a href="http://www.kernel.org/pub/software/scm/git/docs/git-tag.html">Git documentation about tag</a></p>
 *
 * <p><a href="http://download.eclipse.org/jgit/docs/latest/apidocs/org/eclipse/jgit/api/TagCommand.html">JGit TagCommand</a></p>
 *
 * @author Yves Zoundi
 */
public class TagTask extends AbstractGitRepoAwareTask {

        private String name;
        private String message;
        private static final String TASK_NAME = "git-tag-add";
        private static final String MESSAGE_TAG_CREATE_FAILED = "Could not create tag %s";
        private static final String TAG_MESSAGE_TEMPLATE = "Creating tag '%s'";

        @Override
        public String getName() {
                return TASK_NAME;
        }

        /**
         * Sets the tag creation message.
         *
         * @antdoc.notrequired
         * @param message The message
         */
        public void setMessage(String message) {
                this.message = message;
        }

        /**
         * Sets the Git Tag name
         *
         * @param name The tag name
         */
        public void setName(String name) {
                this.name = name;
        }

        @Override
        protected void doExecute() {
                if (message == null) {
                        message = String.format(TAG_MESSAGE_TEMPLATE, name);
                }

                message = GitTaskUtils.BRANDING_MESSAGE + " " + message;

                GitSettings gitSettings = lookupSettings();

                if (gitSettings == null) {
                        throw new MissingRequiredGitSettingsException();
                }

                try {
                        git.tag().setName(name).setTagger(gitSettings.getIdentity()).setMessage(message).call();
                } catch (GitAPIException ex) {
                        throw new GitBuildException(String.format(MESSAGE_TAG_CREATE_FAILED, name), ex);
                }
        }
}
