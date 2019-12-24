Vue.prototype.$ELEMENT = { size: 'small'};
let checkContent = (rule, value, callback) => {
    if (vm.courseList.length==0) {
        return callback(new Error('请添加课时'));
    }else{
        for(let i=0;i<vm.courseList.length;i++){
            if(!vm.courseList[i].sourceId){
                return callback(new Error('不能存在素材为空的课时，请添加素材'));
            }
        }
    }
    callback();
};

var vm = new Vue({
    el:'#rrapp',
    data:{
        showList:1,
        titleLike:'',
        tableData:[],
        pageNum:1,
        pageSize:10,
        total:0,
        loading:false,
        course:{
            id:'',
            title:'新建课程',
            name:'',
            brief:'',
            channel:[],
            img:'',
        },
        upheaders:{
            'token':localStorage.getItem("token")
        },
        channelOptions:[],
        channelProps:{
            value:'id',
            label:'name',
            children:'collegeClassCategoryList'
        },
        rules:{
            name:[
                { required:true,message:'请输入标题',trigger: ["change"] },
                { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' }
            ],
            brief:[
                { required:true,message:'请输入概述内容（500字以内）',trigger: ["change"]},
                { max: 500, message: '长度在500字以内', trigger: 'blur' },
            ],
            img:[
                { required:true ,message:'请上传图片'},
            ],
            content:[
                { required:true ,validator:checkContent,trigger: ["change","blur"]},
            ]
        },
        courseList:[],
        fullscreenLoading:false,
        selDialog:false,
        addMaterialIndex:0,
        searchMaterial:'',
        materialData:[],
        materialNum:1,
        materialTotal:0,
        //编辑素材
        editDialog:false,
        editMeter:{
            type:'',
            name:'',
            sourceId:'',
        },
        editShow:true,
        //学习情况
        studyDialog:false,
        studyStatus:'',
        studyOptions: [{value: '', label: '全部'},
            {value: 'finish', label: '已完成'},
            {value: 'ing', label: '学习中'},
            {value: 'no', label: '未学习'}],
        studyData:[],
        studyNum:1,
        studyTotal:0,
        classId:'',
    },
    methods: {
        handleCurrentChange(val){
            this.pageNum = val;
            this.getList();
        },
        getList(){
            this.loading=true;
            axios.post('/college/class/queryByAll',{
                pageNum:this.pageNum,
                pageSize:this.pageSize,
                title:this.titleLike,
            }).then(res=>{
                this.loading=false;
                if(res.data.code==0){
                    this.tableData = res.data.pageInfo.list;
                    this.total=res.data.pageInfo.total;
                }else{
                    this.$message.error(res.data.msg);
                }
            }).catch(()=>{
                this.loading=false;
            })
        },
        addCourse(){
            this.showList=2;
        },
        editCourse(id){
            this.fullscreenLoading=true;
            this.showList=2;
            this.course.title='编辑课程';
            axios.post('/college/class/info',{
                id
            }).then(res=>{
                this.fullscreenLoading=false;
                if(res.data.code==0){
                    let data = res.data.data;
                    this.course={
                        id:data.id,
                        title:'编辑课程',
                        name:data.title,
                        brief:data.briefIntro,
                        channel:data.categoryIds,
                        img:data.coverUrl,
                    };
                    this.courseList=data.collegeClassSourceList;
                }else{
                    this.$message.error(res.data.msg);
                }
            }).catch(()=>{
                this.fullscreenLoading=false;
                this.$message.error('未获取到课程详情，请稍后再试');
            })
        },
        beforeAvatarUpload(file) {
            const isJPG = file.type;
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (isJPG.indexOf('image')==-1) {
                this.$message.error('只能上传图片');
                return false;
            }
            if (!isLt2M) {
                this.$message.error('上传封面图片大小不能超过 2MB!');
                return false;
            }
        },
        handleAvatarSuccess(res, file) {
            if(res.code!=0){
                this.$message.error(req.msg);
                this.$refs['fileUpload1'].clearFiles();
            }else{
                this.course.img = res.url;
            }
            //this.form.imageUrl = URL.createObjectURL(file.raw);
        },
        addClass(){
            this.courseList.push({name:'',sourceId:'',type:''})
        },
        delClass(index){
            this.courseList.splice(index,1)
        },
        addMaterial(index){
            this.selDialog=true;
            this.getMaterial();
            this.addMaterialIndex=index;
        },
        editMaterial(index){
            this.editDialog=true;
            this.editMeter=this.courseList[index];
            this.getMaterial();
            this.addMaterialIndex=index;
        },
        getMaterial(){
            axios.post('/college/source/queryByAll',{
                pageNum:this.materialNum,
                pageSize:10,
                name:this.searchMaterial,
                relateStatus:'',
                type:''
            }).then(res=>{
                if(res.data.code==0){
                    this.materialData = res.data.pageInfo.list;
                    this.materialTotal=res.data.pageInfo.total;
                }else{
                }
            }).catch(()=>{
            })
        },

        materialCurrentChange(val){
            this.materialNum=val;
            this.getMaterial();
        },
        add(index){
            //this.courseList[this.addMaterialIndex].sourceId=id;
            //this.courseList[this.addMaterialIndex].name=name;
            this.courseList[this.addMaterialIndex]=this.materialData[index];
            this.searchMaterialClose();
        },
        replace(index){
            this.editShow=true;
            this.editMeter=this.materialData[index];
        },
        searchMaterialClose(){
            this.selDialog=false;
            this.close();
        },
        close(){
            this.materialData=[];
            this.materialNum=1;
            this.materialTotal=0;
            this.searchMaterial='';
            this.addMaterialIndex=0;
        },
        editCom(){
            this.courseList[this.addMaterialIndex]=this.editMeter;
            this.editClose();
        },
        editClose(){
            this.editDialog=false;
            this.close();
        },

        //添加或修改课程
        commit(publish){
            this.$refs["course"].validate((valid) => {
                if(!valid){
                    return false;
                } else{
                    let sourceIds=[];
                    this.courseList.map(item=>{
                        sourceIds.push(item.sourceId)
                    });
                    this.fullscreenLoading=true;
                    axios.post('/college/class/editClass',{
                        title:this.course.name,
                        briefIntro:this.course.brief,
                        categoryIds:this.course.channel,
                        id:this.course.id,
                        publish:publish,
                        coverUrl:this.course.img,
                        sourceIds:sourceIds.join(',')
                    }).then(res=>{
                        this.fullscreenLoading=false;
                        if(res.data.code==0){
                            this.$message.success('提交成功');
                            this.goBack();
                            this.getList();
                        }else{
                            this.$message.error(res.data.msg);
                        }
                    }).catch( err =>{
                        console.log(err)
                        this.fullscreenLoading=false;
                        this.$message.error('提交失败');
                    })
                }
            });
        },
        goBack(){
            this.showList=1;
            this.course={
                id:'',
                title:'新建课程',
                name:'',
                brief:'',
                channel:[],
                img:'',
            };
            this.courseList=[];
        },

        getDirectoryList(){
            this.channelOptions=[];
            axios.post('/college/category/queryCategory').then(res=>{
                if(res.data.code==0){
                    this.channelOptions=res.data.data;
                }
            }).catch(()=>{
            })
        },
        handleCourse(id,type){
            let text ='',
                url='';
            if(type=='publish'){
                text='确定要发布该课程吗？';
                url='/college/class/publish';
            }else if(type=='out'){
                text='确定要下架该课程吗？';
                url='/college/class/publish';
            }else{
                text='确定要删除该课程吗？';
                url='/college/class/del';
            }
            this.$confirm(text, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.post(url,{
                    id
                }).then(res=>{
                    if(res.data.code==0){
                        this.$message.success( '操作成功!');
                        this.getList();
                    }else{
                        this.$message.error( '操作失败!');
                    }
                });

            }).catch(() => {
            });
        },

        openStudyData(id,status){
            this.studyStatus=status;
            this.classId=id;
            this.studyDialog=true;
            this.studyCurrentChange(1);
        },
        studyCurrentChange(val){
            this.studyNum=val;
            this.getStudyData();
        },
        getStudyData(){
            axios.post('/college/statisticsStudy/query',{
                pageNum:this.studyNum,
                pageSize:this.pageSize,
                status:this.studyStatus,
                classId:this.classId
            }).then(res=>{
                this.loading=false;
                if(res.data.code==0){
                    this.studyData = res.data.pageInfo.list;
                    this.studyTotal=res.data.pageInfo.total;
                }else{
                    this.$message.error(res.data.msg);
                }
            }).catch(()=>{
                this.loading=false;
            })
        },
        studyClose(){
            this.studyDialog=false;
            this.studyNum=1;
            this.studyStatus='';
            this.classId='';
            this.studyTotal=0;
            this.studyData=[];
        },
        goDirector(){
            parent.window.location.hash = '#modules/college/courseDirectoryManage.html';
        }

    },
    created(){
        this.getList();
        this.getDirectoryList();
    }
});