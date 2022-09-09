import Interfaces.TaskManager;

abstract class TaskManagerTest<T extends TaskManager> {

    public T taskManager;
    
    public void setUp() {
        taskManager.add(new Task
        taskManager.add(new Task       
        taskManager.add(new Epic
        taskManager.add(new SubTask 
                        
    
     @BeforeEach
      public void addManager() {
          taskManager = new InMemoryTaskManager();
          setUp();
      }
      
      @Test
        void getTaskTest() { //getTask 1.1        
            Task testTask1 = taskManager.getTask(0); //кладём таск в список
            Task task = taskManager.getTask(0);
            Assertions.assertEquals(task, testTask1);

            Task testTask1 = taskManager.getTask(2); // не создаём таск и проверяем на null //getTask 1.2
            Assertions.assertNull(testTask1);
       
        }    

    @Test
    void getTaskByIdTest() { //getTaskById + history 2.1
        
        Task testTask1 = taskManager.getTaskById(0); //кладём таск в список
        List<Task> list = taskManager.historyManager.getHistory();
        Task task = taskManager.getTasks().get(0);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(task, testTask1);
        Assertions.assertEquals(list.get(0), testTask1);
        
        Task testTask1 = taskManager.getTaskById(100);
        List<Task> list = taskManager.historyManager.getHistory();
        Assertions.assertTrue(list.isEmpty());        
    }

    
                   
                        
    


}
