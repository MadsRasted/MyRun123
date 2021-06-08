package com.example.myrun.repo;

import android.widget.TextView;

import com.example.myrun.Updatable;
import com.example.myrun.model.Profile;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repo {

    private static Repo repo = new Repo();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    public List<Profile> profiles = new ArrayList<>();
    private final String PROFILES = "profiles";
    private Updatable activity;
    public static Repo r(){ return repo; }

    public void setup(Updatable a, List<Profile> list){
        activity = a;
        profiles = list;
        startListener();
    }

    public Profile getProfileWith(String id){
        for(Profile profile : profiles){
            if(profile.getId().equals(id)){
                return profile;
            }
        }
        return null;
    }

    public void startListener(){
        db.collection(PROFILES).addSnapshotListener((values, error) ->{
            profiles.clear();
            for(DocumentSnapshot snap : values.getDocuments()){
                profiles.add(new Profile(snap.getId()));
            }
            activity.update(null);
        });
    }

    public void profilInfo(TextView name, TextView email, TextView age){
        DocumentReference reference = db.collection("profiles").document();
        Map<String, TextView> map = new HashMap<>();
        map.put("name", name);
        reference.set(map);
        map.put("email", email);
        reference.set(map);
        map.put("age", age);
        reference.set(map);

        System.out.println("Indsat bruger info " + reference.getId());
    }
}
