import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    /**
     * There is a busy intersection between two one-way streets: Main Street and 1st Avenue. Cars passing through
     * the intersection can only pass through one at a time. When multiple cars arrive at the intersection at the same
     * time, two queues can build up - one for each street. Cars are added to the queue in the order at which they
     * arrive. Unfortunately, there is no traffic light to help control traffic when multiple cars arrive at the same
     * time. So, the local residents have devised their own system for determining which car has priority to pass
     * through the intersection:
     *
     * - If in the previous second, no car passed through the intersection, then the first car in the queue for
     *   1st Avenue goes first.
     * - If in the previous second, a car passed through the intersection on 1st Avenue, then the first car in queue
     *   for 1st Avenue goes first.
     * - If in the previous second, a car passed through the intersection on Main Street, then the first car in the
     *   queue for Main Street goes first.
     *
     * Passing through the intersection takes 1 second.
     * For each car, find the time when they will pass through the intersection.
     *
     * @param arrival an array of n integers where the value at index i is the time in seconds
     *                when the ith car arrives at the intersection. If arrival[i] = arrival[j] and i < j,
     *                then car i arrives before car j.
     * @param street an array of n integers where the value at index i is the street on which the ith car is traveling:
     *               0 for Main Street and 1 for 1st Avenue.
     * @return int[n] an array of n integers where the value at index i is the time when the ith car will pass through
     * the intersection
     */
    private static int[] getResult(int[] arrival, int[] street) {
        int[] solution = new int[] {-1, -1, -1, -1};

        HashMap<Integer, Integer> carsPerSecond = new HashMap<>();

        Queue<Integer> firstStreet = new LinkedList<>();
        Queue<Integer> mainStreet = new LinkedList<>();

        // build up the car queue for the streets
        for (int car = 0; car < street.length; car++) {
            if (street[car] == 1) {
                firstStreet.offer(car);
            } else if (street[car] == 0) {
                mainStreet.offer(car);
            }
            // build up the cars per second hashmap containing the frequency of cars per second
            if (carsPerSecond.containsKey(arrival[car])) {
                carsPerSecond.put(arrival[car], carsPerSecond.get(arrival[car]) + 1);
            } else {
                carsPerSecond.put(arrival[car], 1);
            }
        }

        System.out.println("1st st = " + firstStreet);
        System.out.println("Main st = " + mainStreet);

        int previousCrossedCar = -1;

        // go through every second between 0 and the last arrival time
        for (int t = 0; t <= arrival[arrival.length - 1]; t++) {
            int firstStreetCar = firstStreet.peek() == null ? -1 : firstStreet.peek();
            int mainStreetCar = mainStreet.peek() == null ? -1 : mainStreet.peek();

            System.out.println("t = " + t + ", firstCar = " + firstStreetCar + ", mainCar = " + mainStreetCar);

            // check if there are cars that arrived at this time, they will pass first
            if (carsPerSecond.containsKey(t)) {
                /*
                    if in the previous second, no car passed through the intersection,
                    then the first car in the queue for 1st ave goes first
                */
                if (previousCrossedCar == -1 && firstStreetCar != -1) {
                    previousCrossedCar = firstStreet.poll();
                } else if (previousCrossedCar > -1 && street[previousCrossedCar] == 1 && firstStreetCar != -1) {
                /*
                    if in the previous second, a car passed through the intersection on 1st ave,
                    then the first car in the queue for 1st ave goes first
                 */
                    previousCrossedCar = firstStreet.poll();
                } else if (previousCrossedCar > -1 && street[previousCrossedCar] == 0 && mainStreetCar != -1) {
                /*
                    if in the previous second, a car passed through the intersection on main st,
                    then the first car in the queue for main st goes first
                 */
                    previousCrossedCar = mainStreet.poll();
                } else {
                    if (mainStreetCar != -1) {
                        previousCrossedCar = mainStreet.poll();
                    } else {
                        previousCrossedCar = -1;
                    }
                }
                if (previousCrossedCar > -1) {
                    solution[previousCrossedCar] = t;
                }
            } else {
                /*
                    there are no cars that arrived at this time,
                    so let queued up cars pass as long as arrived before the current time
                 */
                if (firstStreetCar != -1 && t >= arrival[firstStreetCar]) {
                    previousCrossedCar = firstStreet.poll();
                    solution[previousCrossedCar] = t;
                } else if (mainStreetCar != -1 && t >= arrival[mainStreetCar]) {
                    previousCrossedCar = mainStreet.poll();
                    solution[previousCrossedCar] = t;
                }
            }

            System.out.println("car " + previousCrossedCar + " passes");
        }

        return solution;
    };

    public static void main(String[] args) {
        System.out.println("Hello world!");
        int[] arrival = new int[] {0, 0, 1, 4};
        int[] street = new int[] {0, 1, 1, 0};
        int[] answer = getResult(arrival, street);
        for (int i = 0; i < answer.length; i++) {
            System.out.println(answer[i]);
        }
    }
}