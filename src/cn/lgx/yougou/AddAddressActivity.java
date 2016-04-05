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
	
	private ArrayAdapter<String> provinceAdapter;//省份Adapter
	private ArrayAdapter<String> cityAdapter;//市Adapter
	private ArrayAdapter<String> countyAdapter;//区县Adapter

	private String[] province_strs = AddressData.PROVINCES;//省份集合
	private String[][] city_strs = AddressData.CITIES;//市集合
	private String[][][] county_strs = AddressData.COUNTIES;//区县集合

	private int provincePosition;// 记录当前省级序号，留给下面修改县级适配器时用

	private String province_str;//保存省份
	private String city_str;//保存市
	private String county_str;//保存县
	
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
		//初始化控件
		ViewUtils.inject(getAct());
	}

	@Override
	public void initDatas() {
		//获得缓存
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
	}

	@Override
	public void initViewOper() {
		// 绑定省份适配器和设置默认值
		provinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, province_strs);
		province.setAdapter(provinceAdapter);
		province.setSelection(0, true); // 设置默认选中项，此处为默认选中第1个值
		// 绑定市适配器和设置默认值
		cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city_strs[0]);
		city.setAdapter(cityAdapter);
		city.setSelection(0, true); // 默认选中第0个
		// 绑定区县适配器和设置默认值
		countyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, county_strs[0][0]);
		county.setAdapter(countyAdapter);
		county.setSelection(0, true);
		//给省份Spinner设置监听
		province.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// 将地级适配器的值改变为city[position]中的值
				cityAdapter = new ArrayAdapter<>(getAct(), android.R.layout.simple_spinner_item, city_strs[position]);
				// 设置二级下拉列表的选项内容适配器
				city.setAdapter(cityAdapter);
				//得到选择的省份
				province_str = province.getSelectedItem().toString();
				provincePosition = position;

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		//给市Spinner设置监听
		city.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				//重新设置Adapter
				countyAdapter = new ArrayAdapter<>(getAct(), android.R.layout.simple_spinner_item,
						county_strs[provincePosition][position]);
				county.setAdapter(countyAdapter);
				//得到选择的市
				city_str = city.getSelectedItem().toString();

			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		//给区县Spinner设置监听
		county.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				//得到选择的区县
				county_str = county.getSelectedItem().toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

	}
	//提交地址的操作
	public void quedingOnClick(View v){
		
		if (!IsTvNull.isNull(etjiedao,etName,etPhone)) {
			toastS("选项填写不完整");
			return;
		}
		
		String jiedao = etjiedao.getText().toString().trim();
		String phone = etPhone.getText().toString().trim();
		String name = etName.getText().toString().trim();
		//得到完整地址信息
		String address = province_str+city_str+county_str+jiedao;
		MyAddressBean mab = new MyAddressBean();
		//给地址对象设置字段
		mab.setUid(ub.getObjectId());
		mab.setInfo(address);
		mab.setName(name);
		mab.setPhone(phone);
		DialogUtils.showDialog(getAct(), null, null, true);
		//将保存好的地址对象上传服务器
		mab.save(getAct(), new SaveListener() {
			
			@Override
			public void onSuccess() {
				DialogUtils.dismiss();
				toastS("地址增加成功");
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				DialogUtils.dismiss();
				toastS(ErrorCodeUtils.getErrorMessage(arg0));
			}
		});
		
	}
	//返回
	@OnClick(R.id.act_adddizhi_back)
	public void backOnClick(View v){
		finish();
	}
}
