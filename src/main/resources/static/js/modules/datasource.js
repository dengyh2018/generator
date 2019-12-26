Vue.prototype.$ELEMENT = {size: 'small'};

var vm = new Vue({
    el: '#rrapp',
    data: {
        tableData: [],
        configData: [],
        name: '',
        type: '',
        url: '',
        username: '',
        pwd: '',
        nameLike: '',
        showList: '1',
        rules: {
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
            id: '',
            name: '',
            type: 'mysql',
            url: '',
            username: '',
            pwd: '',
        },
        config: {
            id: '',
            name: '',
            tables: '',
            models: '',
            datasourceId: '',
            xmlProject: '',
            xmlPackage: '',
            modelProject: '',
            modelPackage: '',
            clientProject: '',
            clientPackage: '',
            serviceProject: '',
            servicePackage: '',
        },
        datasourceId: '',
        tables: '',
        tableList: [],
        configId: '',
        project: {
            newAddress: ''
        },
    },
    methods: {
        handleCurrentChange(val) {
            this.pageNum = val;
            this.getList();
        },
        getList() {
            this.loading = true;
            axios.post('/datasource/list', {
                //pageNum:this.pageNum,
                //pageSize:this.pageSize,
                name: this.nameLike,
            }).then(res => {
                this.loading = false;
                if (res.data.code == 0) {
                    this.tableData = res.data.list;
                } else {
                    this.$message.error(res.data.msg);
                }
            }).catch(() => {
                this.loading = false;
            })
        },
        add() {
            this.showList = 2;
            this.datasource = {
                id: '',
                name: '',
                type: 'mysql',
                url: '',
                username: '',
                pwd: '',
            };
        },
        product(configId) {
            axios.post('/config/product/' + configId, {}).then(res => {
                if (res.data.code == 0) {
                    this.$message.success('生成mybatis文件成功!');
                } else {
                    this.$message.error(res.data.msg);
                }
            }).catch(() => {
            })
        },
        editCf(configId) {
            this.fullscreenLoading = true;
            this.showList = 3;
            vm.tableList = loadTables(vm.datasourceId);
            if (!configId) {
                this.config = {
                    id: '',
                    name: '',
                    tables: '',
                    models: '',
                    datasourceId: vm.datasourceId,
                    xmlProject: '',
                    xmlPackage: '',
                    modelProject: '',
                    modelPackage: '',
                    clientProject: '',
                    clientPackage: '',
                    serviceProject: '',
                    servicePackage: '',
                }
                return;
            }
            axios.get('/config/info/' + configId).then(function (req) {
                vm.fullscreenLoading = false;
                var data = req.data;
                if (data.code != 0) {
                    this.$message.error(data.msg);
                    return;
                }
                if (data.data) {
                    vm.config = data.data;
                    vm.config.tables = vm.config.tables.split(',');
                }
                /*var datasource=data.data;
                this.datasource.id=datasource.id;
                this.datasource.name=datasource.name;
                this.datasource.type=datasource.type;
                this.datasource.url=datasource.url;
                this.datasource.username=datasource.username;
                this.datasource.pwd=datasource.pwd;*/
            });
        },
        editDs(id) {
            this.fullscreenLoading = true;
            this.showList = 2;
            axios.get('/datasource/info/' + id).then(function (req) {
                vm.fullscreenLoading = false;
                var data = req.data;
                if (data.code != 0) {
                    this.$message.error(data.msg);
                    return;
                }
                vm.datasource = data.data;
                /*var datasource=data.data;
                this.datasource.id=datasource.id;
                this.datasource.name=datasource.name;
                this.datasource.type=datasource.type;
                this.datasource.url=datasource.url;
                this.datasource.username=datasource.username;
                this.datasource.pwd=datasource.pwd;*/
            });
        },
        edit() {
            axios.post('/datasource/edit', {
                id: vm.datasource.id,
                name: this.datasource.name,
                type: this.datasource.type,
                url: this.datasource.url,
                username: this.datasource.username,
                pwd: this.datasource.pwd
            }).then(res => {
                this.fullscreenLoading = false;
                if (res.data.code == 0) {
                    this.$message.success('提交成功');
                    this.goBack();
                    this.getList();
                } else {
                    this.$message.error(res.data.msg);
                }
            }).catch(err => {
                console.log(err)
                this.fullscreenLoading = false;
                this.$message.error('提交失败');
            })
        },
        editCfInfo() {
            axios.post('/config/edit', {
                id: vm.config.id,
                datasourceId: vm.datasourceId,
                name: this.config.name,
                tables: this.config.tables.join(','),
                models: this.config.models,
                xmlProject: this.config.xmlProject,
                xmlPackage: this.config.xmlPackage,
                modelProject: this.config.modelProject,
                modelPackage: this.config.modelPackage,
                clientProject: this.config.clientProject,
                clientPackage: this.config.clientPackage,
                serviceProject: this.config.serviceProject,
                servicePackage: this.config.servicePackage
            }).then(res => {
                this.fullscreenLoading = false;
                if (res.data.code == 0) {
                    this.$message.success('提交成功');
                    this.goBack2();
                    this.cfList();
                } else {
                    this.$message.error(res.data.msg);
                }
            }).catch(err => {
                console.log(err)
                this.fullscreenLoading = false;
                this.$message.error('提交失败');
            })
        },
        cfList(datasourceId) {
            if (!datasourceId) {
                datasourceId = vm.datasourceId;
            }
            this.loading = true;
            this.datasourceId = datasourceId;
            this.showList = 4;
            axios.post('/config/getByDatasourceId/' + datasourceId, {
                //pageNum:this.pageNum,
                //pageSize:this.pageSize,
                name: this.nameLike2,
            }).then(res => {
                this.loading = false;
                if (res.data.code == 0) {
                    this.configData = res.data.configList;
                } else {
                    this.$message.error(res.data.msg);
                }
            }).catch(() => {
                this.loading = false;
            })
        },
        goBack() {
            this.showList = 1;
            this.getList();
            /*this.datasource={
                id:'',
                name:'',
                type:'mysql',
                url:'',
                username:'',
                pwd:'',
            };*/
        },
        goBack2() {
            this.showList = 4;
            this.cfList(vm.datasourceId);
        },
        del(id) {
            let text = '确定要删除该数据源吗？',
                url = '/datasource/del/' + id;
            this.$confirm(text, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.post(url).then(res => {
                    if (res.data.code == 0) {
                        this.$message.success('操作成功!');
                        this.getList();
                    } else {
                        this.$message.error('操作失败!');
                    }
                });
            }).catch(() => {
            });
        },
        delCf(id) {
            let text = '确定要删除该模块吗？',
                url = '/config/del/' + id;
            this.$confirm(text, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.post(url).then(res => {
                    if (res.data.code == 0) {
                        this.$message.success('操作成功!');
                        this.cfList();
                    } else {
                        this.$message.error('操作失败!');
                    }
                });
            }).catch(() => {
            });
        },
        pj(configId) {
            vm.configId = configId;
            this.project.newAddress = '';
            this.showList = 5;
        },
        editPj() {
            axios.post('/project/edit', {
                configId: vm.configId,
                newAddress: this.project.newAddress,
            }).then(res => {
                this.fullscreenLoading = false;
                if (res.data.code == 0) {
                    this.$message.success('项目地址提交成功');
                    this.goBack2();
                    this.cfList();
                } else {
                    this.$message.error(res.data.msg);
                }
            }).catch(err => {
                console.log(err)
                this.fullscreenLoading = false;
                this.$message.error('项目地址提交失败');
            })
        },
    },
    created() {
        this.getList();
    },
    computed: {}
});