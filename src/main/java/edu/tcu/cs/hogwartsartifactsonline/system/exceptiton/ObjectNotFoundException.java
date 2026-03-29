package edu.tcu.cs.hogwartsartifactsonline.system.exceptiton;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String objectName, String id)
    {
        super("Could not find " + objectName + " with Id " + id + " :(");
    }

    public ObjectNotFoundException(String objectName, Integer id) {
        super("Could not find " + objectName + " with Id " + id + " :(");
    }
}
