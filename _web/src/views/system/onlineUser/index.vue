<template>
  <a-card :bordered="false">

        <a-table
          ref="table"
          size="default"
          :pagination="false"
          :loading="loading"
          :columns="columns"
          :dataSource="loadData"
          :rowKey="(record) => record.sessionId"
          :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        >
          <span slot="action" slot-scope="text, record">
            <a-popconfirm v-if="hasPerm('sysOnlineUser:forceExist')" placement="topRight"   title="是否强制下线该用户？" @confirm="() => forceExist(record)">
              <a>强制下线</a>
            </a-popconfirm>
          </span>
        </a-table>

  </a-card>
</template>

<script>
  import { sysOnlineUserForceExist ,sysOnlineUserList } from '@/api/modular/system/onlineUserManage'

  export default {
    components: {
    },

    data () {
      return {

        // 高级搜索 展开/关闭
        advanced: false,
        // 查询参数
        queryParam: {},
        // 表头
        columns: [
          {
            title: '账号',
            dataIndex: 'account'
          },
          {
            title: '昵称',
            dataIndex: 'nickName'
          },
          {
            title: '最后登录IP',
            dataIndex: 'lastLoginIp'
          },
          {
            title: '最后登录时间',
            dataIndex: 'lastLoginTime'
          },
          {
            title: '最后登录地址',
            dataIndex: 'lastLoginAddress'
          },
          {
            title: '最后登录浏览器',
            dataIndex: 'lastLoginBrowser'
          },
          {
            title: '最后登录所用系统',
            dataIndex: 'lastLoginOs'
          }
        ],
        loading:true,
        loadData:[],
        selectedRowKeys: [],
        selectedRows: [],
    }
    },

    //进页面加载
    created () {
      this.loadDataList()
      if(this.hasPerm('sysOnlineUser:forceExist')){
        this.columns.push({
          title: '操作',
          width: '150px',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        })
      }
    },

    methods: {

      //加载数据方法
      loadDataList(){
        if(!this.hasPerm('sysOnlineUser:list')){
          this.loading=false
          this.loadData=null
          this.$message.error('无权限查询数据，请联系管理员')
          return
        }
        sysOnlineUserList().then((res)=>{
          this.loading=false
          this.loadData=res.data
        })
      },

      forceExist(record){
        sysOnlineUserForceExist(record).then((res)=>{
          if(res.success) {
            this.$message.success('强制下线成功')
            //重新加载表格
            this.loadDataList()
          }else{
            this.$message.error('强制下线失败：'+res.message)
          }
        }).catch((err)=>{
          this.$message.error('强制下线错误：'+err.message)
        })
      },

      onSelectChange (selectedRowKeys, selectedRows) {
        this.selectedRowKeys = selectedRowKeys
        this.selectedRows = selectedRows
      }
    }

  }
</script>

<style lang="less">
  .table-operator {
    margin-bottom: 18px;
  }
  button {
    margin-right: 8px;
  }

</style>
