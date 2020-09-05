package box2d;

import enums.UserDataType;

public class GroundUserData extends UserData{
	public GroundUserData(float width, float height) {
        super(width, height);
        userDataType = UserDataType.GROUND;
    }
}
