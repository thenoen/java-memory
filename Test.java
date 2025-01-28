
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Test {

    public static void main(String[] args) throws InterruptedException {

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        printMemoryInfo(memoryMXBean);

        List<MemoryPoolMXBean> memoryBeans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean mBean : memoryBeans) {
            // System.out.println("memory bean: " + mBean.getName() + " / " + mBean.getClass());
            printMemoryPoolInfo(mBean);
            
            System.out.println("");

        }

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
            printMemoryInfo(memoryMXBean);
            System.out.println("used heap memory: " + usagePercent + "%");
            System.out.println();

        }

    }

    private static void printMemoryInfo(MemoryMXBean mb) {
        final MemoryUsage heapMemoryUsage = mb.getHeapMemoryUsage();
        final MemoryUsage nonHeapMemoryUsage = mb.getNonHeapMemoryUsage();

        System.out.println("heap: " + heapMemoryUsage);
        System.out.println("non-heap: " + nonHeapMemoryUsage);

        System.out.println("================================");

        System.out.println("Initial heap memory usage:\t\t" + toMiB(heapMemoryUsage.getInit()) + " MiB");
        System.out.println("Used heap memory usage:\t\t\t" + toMiB(heapMemoryUsage.getUsed()) + " MiB");
        System.out.println("Committed heap memory usage:\t\t" + toMiB(heapMemoryUsage.getCommitted()) + " MiB");
        System.out.println("Max heap memory usage:\t\t\t" + toMiB(heapMemoryUsage.getMax()) + " MiB");

        System.out.println("--------------------------------");

        System.out.println("Initial non-heap memory usage:\t\t" + toMiB(nonHeapMemoryUsage.getInit()) + " MiB");
        System.out.println("Used non-heap memory usage:\t\t" + toMiB(nonHeapMemoryUsage.getUsed()) + " MiB");
        System.out.println("Committed non-heap memory usage:\t" + toMiB(nonHeapMemoryUsage.getCommitted()) + " MiB");
        System.out.println("Max non-heap memory usage:\t\t" + toMiB(nonHeapMemoryUsage.getMax()) + " MiB");

        System.out.println("================================");
        System.out.println("total commited memory: " + toMiB(heapMemoryUsage.getCommitted() + nonHeapMemoryUsage.getCommitted()) + " MiB");
    }

    private static void printMemoryPoolInfo(MemoryPoolMXBean memoryPoolMXBean) {
        // MemoryUsage memoryUsage = memoryPoolMXBean.getCollectionUsage();
        MemoryUsage memoryUsage = memoryPoolMXBean.getUsage();

        System.out.println("=============== " + memoryPoolMXBean.getName() + " =================");

        if (memoryUsage == null) {
            System.out.println("N/A");
            return;
        }

        System.out.println("Initial heap memory usage:\t\t" + toMiB(memoryUsage.getInit()) + " MiB");
        System.out.println("Used heap memory usage:\t\t\t" + toMiB(memoryUsage.getUsed()) + " MiB");
        System.out.println("Committed heap memory usage:\t\t" + toMiB(memoryUsage.getCommitted()) + " MiB");
        System.out.println("Max heap memory usage:\t\t\t" + toMiB(memoryUsage.getMax()) + " MiB");

        System.out.println("\nmemory managers: ");
        for (int i = 0; i < memoryPoolMXBean.getMemoryManagerNames().length; i++) {
                System.out.println("\t" + memoryPoolMXBean.getMemoryManagerNames()[i]);
            }

        System.out.println("=====================================================================");
    }

    private static long getUsedHeapPercentage() {
        MemoryMXBean mb = ManagementFactory.getMemoryMXBean();
        long maxHeap = mb.getHeapMemoryUsage().getMax();
        long usedHeap = mb.getHeapMemoryUsage().getUsed();
        long usagePercent = (long) ((usedHeap / (float) maxHeap) * 100);
        return usagePercent;
    }

    private static long toMiB(long bytes) {
        return bytes / (1024 * 1024);
    }

    private static class ValueHolder<T> {

        T value;
    }
}
