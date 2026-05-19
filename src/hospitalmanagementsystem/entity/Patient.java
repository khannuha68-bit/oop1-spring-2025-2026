package hospitalmanagementsystem.entity;

public class Patient {

    private String id;
    private String name;
    private String age;
    private String disease;

    public Patient(String id, String name, String age, String disease) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.disease = disease;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getDisease() {
        return disease;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String toLine() {
        return id + "," + name + "," + age + "," + disease;
    }

    public static Patient fromLine(String line) {

        if (line == null)
            return null;

        String[] data = line.split(",", -1);

        if (data.length != 4)
            return null;

        return new Patient(data[0], data[1], data[2], data[3]);
    }

    public Object[] toRow() {
        return new Object[] { id, name, age, disease };
    }
}