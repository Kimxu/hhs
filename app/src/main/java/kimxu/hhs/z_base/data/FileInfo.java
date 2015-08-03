package kimxu.hhs.z_base.data;


public class FileInfo implements GenericData {
	public final long lastModified;
	public final long created;
	public final long size;
	public FileInfo(long lastModified, long created, long size) {
		this.lastModified = lastModified;
		this.created = created;
		this.size = size;
	}
	
	@Override
	public String getIdentifyer() {
		// TODO Auto-generated method stub
		return "FileInfo";
	}
	
}
