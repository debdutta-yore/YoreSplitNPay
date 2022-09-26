package co.yore.splitnpay.object_box;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class User { 
    @Id
    long id;

    String name;
}