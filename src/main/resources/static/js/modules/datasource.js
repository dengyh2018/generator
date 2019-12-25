Vue.prototype.$ELEMENT = { size: 'small'};

var vm = new Vue({
    el:'#rrapp',
    data:{
        tableData: [],
        name:'',
        type:'',
        url:'',
        username:'',
        pwd:'',
        nameLike:'',
        showList:'1',
        rules:{
            /*name:[
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
                { required:true,trigger: ["change","blur"]},
            ]*/
        },
        typeSelect: [{
            value: 'mysql',
            label: 'mysql'
        }, {
            value: 'oracle',
            label: 'oracle'
        }],
        datasource: {
            id:'',
            name: '',
            type: 'mysql',
            url: '',
            username: '',
            pwd: '',
        },
    },
    methods: {
        handleCurrentChange(val){
            this.pageNum = val;
            this.getList();
        },
        getList(){
            this.loading=true;
            axios.post('/datasource/list',{
                //pageNum:this.pageNum,
                //pageSize:this.pageSize,
                name:this.nameLike,
            }).then(res=>{
                this.loading=false;
                if(res.data.code==0){
                    this.tableData = res.data.list;
                }else{
                    this.$message.error(res.data.msg);
                }
            }).catch(()=>{
                this.loading=false;
            })
        },
        add(){
            this.showList=2;
        },
        editDs(id){
            this.fullscreenLoading=true;
            this.showList=2;
            axios.get('/datasource/info/' + id).then(function (req) {
                vm.fullscreenLoading = false;
                var data = req.data;
                if (data.code != 0) {
                    alert(data.msg);
                    return;
                }
                vm.datasource=data.data;
                /*var datasource=data.data;
                this.datasource.id=datasource.id;
                this.datasource.name=datasource.name;
                this.datasource.type=datasource.type;
                this.datasource.url=datasource.url;
                this.datasource.username=datasource.username;
                this.datasource.pwd=datasource.pwd;*/
            });
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
        edit(){
            axios.post('/datasource/edit',{
                id:vm.datasource.id,
                name:this.datasource.name,
                type:this.datasource.type,
                url:this.datasource.url,
                username:this.datasource.username,
                pwd:this.datasource.pwd
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
            this.datasource={
                id:'',
                name:'',
                type:'mysql',
                url:'',
                username:'',
                pwd:'',
            };
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
        //this.getDirectoryList();
    }
});