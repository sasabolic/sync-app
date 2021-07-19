package com.example.domain.model;

import lombok.Value;

@Value
public class FetchObjectDetailsCommand {

  DataObjectId dataObjectId;

  DataObjectType dataObjectType;

  Authentication authentication;
}
