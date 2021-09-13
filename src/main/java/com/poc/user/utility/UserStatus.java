package com.poc.user.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@AllArgsConstructor
@ToString
public enum UserStatus {
	
	ACTIVE(1), INACTIVE(2);
	
	private int statusValue;
	
	public static UserStatus GetUserStatusByValue(int status) 
	{
			for(UserStatus obj:UserStatus.values()) 
			{
				if(obj.statusValue == status) {
					return obj;
				}
			}
			return null;
	}
	
}
