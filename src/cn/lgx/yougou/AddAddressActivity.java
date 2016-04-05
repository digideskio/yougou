package cn.lgx.yougou;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.MyAddressBean;
import cn.lgx.yougou.bean.UserBean;
import cn.lgx.yougou.utils.AddressData;
import cn.lgx.yougou.utils.DialogUtils;
import cn.lgx.yougou.utils.ErrorCodeUtils;
import cn.lgx.yougou.utils.IsTvNull;

public class AddAddressActivity extends BaseActivity implements BaseInterface {
	@ViewInject(R.id.spin_province)
	private Spinner province;
	@ViewInject(R.id.spin_city)
	private Spinner city;
	@ViewInject(R.id.spin_county)
	private Spinner county;
	
	private ArrayAdapter<String> provinceAdapter;//ʡ��Adapter
	private ArrayAdapter<String> cityAdapter;//��Adapter
	private ArrayAdapter<String> countyAdapter;//����Adapter

	private String[] province_strs = AddressData.PROVINCES;//ʡ�ݼ���
	private String[][] city_strs = AddressData.CITIES;//�м���
	private String[][][] county_strs = AddressData.COUNTIES;//���ؼ���

	private int provincePosition;// ��¼��ǰʡ����ţ����������޸��ؼ�������ʱ��

	private String province_str;//����ʡ��
	private String city_str;//������
	private String county_str;//������
	
	@ViewInject(R.id.act_adddizhi_jiedao)
	private EditText etjiedao;
	@ViewInject(R.id.act_adddizhi_shouhuname)
	private EditText etName;
	@ViewInject(R.id.act_adddizhi_phone)
	private EditText etPhone;
	
	private UserBean ub;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_adddizhi);
		//��ʼ���ؼ�
		ViewUtils.inject(getAct());
	}

	@Override
	public void initDatas() {
		//��û���
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
	}

	@Override
	public void initViewOper() {
		// ��ʡ��������������Ĭ��ֵ
		provinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, province_strs);
		province.setAdapter(provinceAdapter);
		province.setSelection(0, true); // ����Ĭ��ѡ����˴�ΪĬ��ѡ�е�1��ֵ
		// ����������������Ĭ��ֵ
		cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city_strs[0]);
		city.setAdapter(cityAdapter);
		city.setSelection(0, true); // Ĭ��ѡ�е�0��
		// ������������������Ĭ��ֵ
		countyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, county_strs[0][0]);
		county.setAdapter(countyAdapter);
		county.setSelection(0, true);
		//��ʡ��Spinner���ü���
		province.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// ���ؼ���������ֵ�ı�Ϊcity[position]�е�ֵ
				cityAdapter = new ArrayAdapter<>(getAct(), android.R.layout.simple_spinner_item, city_strs[position]);
				// ���ö��������б��ѡ������������
				city.setAdapter(cityAdapter);
				//�õ�ѡ���ʡ��
				province_str = province.getSelectedItem().toString();
				provincePosition = position;

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		//����Spinner���ü���
		city.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				//��������Adapter
				countyAdapter = new ArrayAdapter<>(getAct(), android.R.layout.simple_spinner_item,
						county_strs[provincePosition][position]);
				county.setAdapter(countyAdapter);
				//�õ�ѡ�����
				city_str = city.getSelectedItem().toString();

			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		//������Spinner���ü���
		county.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				//�õ�ѡ�������
				county_str = county.getSelectedItem().toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

	}
	//�ύ��ַ�Ĳ���
	public void quedingOnClick(View v){
		
		if (!IsTvNull.isNull(etjiedao,etName,etPhone)) {
			toastS("ѡ����д������");
			return;
		}
		
		String jiedao = etjiedao.getText().toString().trim();
		String phone = etPhone.getText().toString().trim();
		String name = etName.getText().toString().trim();
		//�õ�������ַ��Ϣ
		String address = province_str+city_str+county_str+jiedao;
		MyAddressBean mab = new MyAddressBean();
		//����ַ���������ֶ�
		mab.setUid(ub.getObjectId());
		mab.setInfo(address);
		mab.setName(name);
		mab.setPhone(phone);
		DialogUtils.showDialog(getAct(), null, null, true);
		//������õĵ�ַ�����ϴ�������
		mab.save(getAct(), new SaveListener() {
			
			@Override
			public void onSuccess() {
				DialogUtils.dismiss();
				toastS("��ַ���ӳɹ�");
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				DialogUtils.dismiss();
				toastS(ErrorCodeUtils.getErrorMessage(arg0));
			}
		});
		
	}
	//����
	@OnClick(R.id.act_adddizhi_back)
	public void backOnClick(View v){
		finish();
	}
}
