import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Test {

    public static void main(String[] args) throws InterruptedException {

        MemoryMXBean mb = ManagementFactory.getMemoryMXBean();
        System.out.println(mb.getHeapMemoryUsage());
        System.out.println(mb.getNonHeapMemoryUsage());

        // ManagementFactory.getGarbageCollectorMXBeans().getUsedHeapPercentage();

        List<ValueHolder<String>> values = new ArrayList<>();

        long allocationRate = 100;
        long freeRate = 100;

        while (true) {

            Thread.sleep(400);

            long usagePercent = getUsedHeapPercentage();

            if (usagePercent < 30) {
                allocationRate += 10;
                freeRate = Math.max(1, Math.abs(freeRate - 10));
            }

            if (usagePercent > 70) {
                allocationRate = Math.max(1, Math.abs(allocationRate - 10));
                freeRate += 10;
            }

            Random r = new Random();

            // long newObjectCount = r.nextLong(0, allocationRate * 1000);
            long newObjectCount = allocationRate * 1000;            

            for (long i = 0; i < newObjectCount; i++) {
                ValueHolder<String> vh = new ValueHolder<>();
                vh.value = new String(System.currentTimeMillis() + "");
                values.add(vh);
            }

            // long deleteObjectCount = r.nextLong(0, Math.min(values.size(), freeRate * 1000));
            long deleteObjectCount = Math.min(values.size(), freeRate * 1000);

            for (int j = 0; j < deleteObjectCount; j++) {
                values.removeLast();
            }

            System.gc(); // <== in this code is very high chance that GC will actually run and has a big effect on memory usage
            usagePercent = getUsedHeapPercentage();

            System.out.println("new object count: " + newObjectCount + " (" + allocationRate + ")");
            System.out.println("delete object count: " + deleteObjectCount + " (" + freeRate + ")");
            System.out.println("object count: " + values.size());
            System.out.println("used memory: " + usagePercent + "%");
            System.out.println();

        }

    }

    private static long getUsedHeapPercentage() {
        MemoryMXBean mb = ManagementFactory.getMemoryMXBean();
        long maxHeap = mb.getHeapMemoryUsage().getMax();
        long usedHeap = mb.getHeapMemoryUsage().getUsed();
        long usagePercent = (long) ((usedHeap / (float) maxHeap) * 100);
        return usagePercent;
    }

    private static class ValueHolder<T> {
        T value;
    }
}