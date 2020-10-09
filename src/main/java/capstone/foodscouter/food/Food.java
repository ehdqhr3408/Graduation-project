package capstone.foodscouter.food;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Food {

    @Id
    private long index;

    private String food;

    private int cal;
}
