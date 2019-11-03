/*
Brandon Franke
B00786125
This class is used to create a queue.
 */

import java.util.ArrayList;

public class  Queue {
    private ArrayList<String> queue;


    public Queue(){
        queue = new ArrayList<>();
    } //creates a new queue

    public void enqueue(String item){ //adds an item to the queue
        queue.add(item);
    }
    public String dequeue(){ //removes the item in line and returns it
        String temp = queue.get(0);
        queue.remove(0);
        return temp;
    }
    public boolean isEmpty(){
        return queue.isEmpty();
    }


    public void enumerate(){
        for (int i = 0; i < queue.size(); i++){
            System.out.print(queue.get(i) + "\t");
        }
    }

    public boolean contains(String item){
        return queue.contains(item);
    }
    public int size(){
        return queue.size();
    }

    public void remove(String item){
        queue.remove(item);
    }
}
