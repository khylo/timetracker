package com.khylo.timetracker.model;

import lombok.*;

import java.util.List;

@Data
@ToString(exclude="id")

@Builder(toBuilder = true) @NoArgsConstructor
@AllArgsConstructor
public class User {
    String id;
    List<UserStaticData> profiles;
}
