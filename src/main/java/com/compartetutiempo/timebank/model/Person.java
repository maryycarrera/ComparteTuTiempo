/*
 * Copyright 2002-2013 the original author or authors.
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
package com.compartetutiempo.timebank.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Simple JavaBean domain object representing a person.
 *
 * @author Ken Krebs
 */
@MappedSuperclass
@Getter
@Setter
public class Person extends BaseEntity {

	@NotEmpty
    @Size(min = 3, max = 100)
	protected String name;

	@Column(name = "last_name")
	@NotEmpty
    @Size(min = 3, max = 100)
	protected String lastName;

    @Column(name = "email", unique = true)
    @NotEmpty
    @Size(max = 255)
    @Email
    protected String email;

    @Column(name = "profile_picture")
    @Size(max = 255)
        @Pattern(regexp = "[\\w./\\\\-]+\\.(png|jpg|jpeg|gif|PNG|JPG|JPEG|GIF)$", message = "The profile picture must end with .png, .jpg, .jpeg, or .gif (case-insensitive)")
    protected String profilePicture;

    public String getFullName() {
        return name + " " + lastName;
    }
}