package hospitalmanagementsystem.fileio;

import hospitalmanagementsystem.entity.Patient;
import java.io.*;

public class PatientFileIO {

    
    private static final String FILE_NAME = "patients.txt";
    private static final String TEMP_FILE = "temp.txt";

    public static void createFileIfNotExists() throws IOException {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public static boolean idExists(String id) {

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = br.readLine()) != null) {

                Patient p = Patient.fromLine(line);

                if (p != null && p.getId().equals(id))
                    return true;
            }

        } catch (IOException ignored) {
        }

        return false;
    }

    public static void addPatient(Patient p) throws IOException {

        try (PrintWriter pw = new PrintWriter(
                new BufferedWriter(new FileWriter(FILE_NAME, true)))) {

            pw.println(p.toLine());
        }
    }

    public static boolean updatePatient(Patient p) throws IOException {

        File inputFile = new File(FILE_NAME);
        File tempFile = new File(TEMP_FILE);

        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;

            while ((line = br.readLine()) != null) {

                Patient existing = Patient.fromLine(line);

                if (existing != null && existing.getId().equals(p.getId())) {

                    bw.write(p.toLine());
                    found = true;

                } else {

                    bw.write(line);
                }

                bw.newLine();
            }
        }

        if (found) {

            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                throw new IOException("Update failed.");
            }

        } else {
            tempFile.delete();
        }

        return found;
    }

    public static boolean deletePatient(String id) throws IOException {

        File inputFile = new File(FILE_NAME);
        File tempFile = new File(TEMP_FILE);

        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;

            while ((line = br.readLine()) != null) {

                Patient existing = Patient.fromLine(line);

                if (existing != null && existing.getId().equals(id)) {
                    found = true;
                    continue;
                }

                bw.write(line);
                bw.newLine();
            }
        }

        if (found) {

            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                throw new IOException("Delete failed.");
            }

        } else {
            tempFile.delete();
        }

        return found;
    }

    public static Object[][] getAllPatients() {

        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            while (br.readLine() != null) {
                count++;
            }
        } catch (IOException ignored) {
        }

        Object[][] data = new Object[count][4];

        int i = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = br.readLine()) != null) {

                Patient p = Patient.fromLine(line);

                if (p != null) {
                    data[i++] = p.toRow();
                }
            }

        } catch (IOException ignored) {
        }

        return data;
    }

    public static Object[][] searchPatients(String keyword) {

        keyword = keyword.toLowerCase();

        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = br.readLine()) != null) {

                Patient p = Patient.fromLine(line);

                if (p != null &&
                        (p.getId().toLowerCase().contains(keyword)
                                || p.getName().toLowerCase().contains(keyword))) {
                    count++;
                }
            }

        } catch (IOException ignored) {
        }

        Object[][] result = new Object[count][4];

        int i = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = br.readLine()) != null) {

                Patient p = Patient.fromLine(line);

                if (p != null &&
                        (p.getId().toLowerCase().contains(keyword)
                                || p.getName().toLowerCase().contains(keyword))) {

                    result[i++] = p.toRow();
                }
            }

        } catch (IOException ignored) {
        }

        return result;
    }
}
