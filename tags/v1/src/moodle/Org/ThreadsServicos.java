package moodle.Org;

public abstract class ThreadsServicos implements Runnable {

	protected boolean mantemAtualizando;
	protected MoodleEnv environment;
	
	public ThreadsServicos(MoodleEnv env){
		this.environment = env;
		mantemAtualizando = true;
	}
	
	public void setMantemAtualizando(boolean m){
		mantemAtualizando = m;
	}
}
