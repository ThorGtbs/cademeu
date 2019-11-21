package Classes;


import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FireBaseInsert {
    static String marker = "Marker";
    private DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public FireBaseInsert(){

    }

    public void enviaFireBase( MarkerSets markerSets){
        int hash = firebaseReference.hashCode() +2;
        firebaseReference.child(marker).child(String.valueOf(hash)).setValue(markerSets);
    }

}
