import java.util.ArrayList;
import java.util.List;

public class Admin extends Account {
    private final List<Lab> labs;

    public Admin(String name, int id, int password) {
        super(name, id, password);
        labs = new ArrayList<>();
        initializeLabs(); //initialize lab data
    }

    private void initializeLabs() {
        // Initialize lab data here
        labs.add(new Lab(25, 20, "C2", false));
        labs.add(new Lab(29, 15, "C2", false));
        labs.add(new Lab(32, 24, "B1", false));
        labs.add(new Lab(37, 22, "B1", false));
        labs.add(new Lab(42, 21, "C1", false));
    }


    public void addLab(Lab lab) {
        // Check if the lab number already exists
        boolean labExists = false;
        for (Lab existingLab : labs) {
            if (existingLab.getNumber() == lab.getNumber()) {
                labExists = true;
                break;
            }
        }

        if (!labExists) {
            labs.add(lab);
            System.out.println("Lab added: " + lab.getNumber());
        } else {
            System.out.println("Lab with number " + lab.getNumber() + " already exists. Lab not added.");
        }

    }


    public List<Lab> getLabs() {
        return labs;
    }
    public void reserveLab(Lab lab, Time bookingDate) {
        for (Lab l : labs) {
            if (l.equals(lab)) {
                l.setStatus(true);
                l.setBookingDate(bookingDate);
                break;
            }
        }
    }



}
