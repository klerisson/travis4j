/**
 *    Copyright 2015-2016 Thomas Rausch
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.travis4j.model;

import java.time.Instant;

/**
 * Repository.
 */
public interface Repository extends Entity {

    Long getId();

    String getSlug();

    String getDescription();

    Boolean getActive();

    String getGithubLanguage();

    Long getLastBuildId();

    Long getLastBuildNumber();

    String getLastBuildState();

    Long getLastBuildDuration();

    String getLastBuildLanguage();

    Instant getLastBuildStartedAt();

    Instant getLastBuildFinishedAt();

}
