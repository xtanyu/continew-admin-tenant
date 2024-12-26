<template>
  <div class="table-page">
    <GiTable
      title="${businessName}管理"
      row-key="id"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1000 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['name']"
      @refresh="search"
    >
      <#-- 查询字段配置 -->
      <template #toolbar-left>
      <#list fieldConfigs as fieldConfig>
      <#if fieldConfig.showInQuery>
	  <#if fieldConfig.formType == "SELECT"><#-- 下拉框 -->
        <a-select
          v-model="queryForm.${fieldConfig.fieldName}"
          :options="${fieldConfig.columnName}_enum"
          placeholder="请选择${fieldConfig.comment}"
          allow-clear
          style="width: 150px"
          @change="search"
        />
	  <#elseif fieldConfig.formType == "RADIO"><#-- 单选框 -->
		<a-radio-group v-model="queryForm.${fieldConfig.fieldName}" :options="${fieldConfig.dictCode!'dictKey 或者自定义数组'}" @change="search"/>
	  <#elseif fieldConfig.formType == "DATE"><#-- 日期框 -->
        <#if fieldConfig.queryType == "BETWEEN">
        <DateRangePicker v-model="queryForm.${fieldConfig.fieldName}" format="YYYY-MM-DD" @change="search" />
        <#else>
        <a-date-picker
          v-model="queryForm.${fieldConfig.fieldName}"
          placeholder="请选择${fieldConfig.comment}"
          format="YYYY-MM-DD"
          style="height: 32px"
        />
        </#if>
      <#elseif fieldConfig.formType == "DATE_TIME"><#-- 日期时间框 -->
        <#if fieldConfig.queryType == "BETWEEN">
        <DateRangePicker v-model="queryForm.${fieldConfig.fieldName}" @change="search" />
        <#else>
        <a-date-picker
          v-model="queryForm.${fieldConfig.fieldName}"
          placeholder="请选择${fieldConfig.comment}"
          show-time
          format="YYYY-MM-DD HH:mm:ss"
          style="height: 32px"
        />
        </#if>
	  <#else>
	    <a-input v-model="queryForm.${fieldConfig.fieldName}" placeholder="请输入${fieldConfig.comment}" allow-clear @change="search">
	      <template #prefix><icon-search /></template>
	    </a-input>
      </#if>
      </#if>
      </#list>
        <a-button @click="reset">
          <template #icon><icon-refresh /></template>
          <template #default>重置</template>
        </a-button>
      </template>
      <template #toolbar-right>
        <a-button v-permission="['${apiModuleName}:${apiName}:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <template #default>新增</template>
        </a-button>
        <a-button v-permission="['${apiModuleName}:${apiName}:export']" @click="onExport">
          <template #icon><icon-download /></template>
          <template #default>导出</template>
        </a-button>
      </template>
      <#list fieldConfigs as fieldConfig>
      <#if fieldConfig.showInList>
      <#if fieldConfig.dictCode?? && fieldConfig.dictCode != "">
      <template #${fieldConfig.fieldName}="{ record }">
        <GiCellTag :value="record.${fieldConfig.fieldName}" :dict="${fieldConfig.dictCode}" />
      </template>
      </#if>
      </#if>
      </#list>
      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['${apiModuleName}:${apiName}:detail']" title="详情" @click="onDetail(record)">详情</a-link>
          <a-link v-permission="['${apiModuleName}:${apiName}:update']" title="修改" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['${apiModuleName}:${apiName}:delete']"
            status="danger"
            :disabled="record.disabled"
            :title="record.disabled ? '不可删除' : '删除'"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <${classNamePrefix}AddModal ref="${classNamePrefix}AddModalRef" @save-success="search" />
    <${classNamePrefix}DetailDrawer ref="${classNamePrefix}DetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import ${classNamePrefix}AddModal from './${classNamePrefix}AddModal.vue'
import ${classNamePrefix}DetailDrawer from './${classNamePrefix}DetailDrawer.vue'
import { type ${classNamePrefix}Resp, type ${classNamePrefix}Query, delete${classNamePrefix}, export${classNamePrefix}, list${classNamePrefix} } from '@/apis/${apiModuleName}/${apiName}'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { useDict } from '@/hooks/app'
import { isMobile } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: '${classNamePrefix}' })

<#if hasDictField>
const { <#list dictCodes as dictCode>${dictCode}<#if dictCode_has_next>,</#if></#list> } = useDict(<#list dictCodes as dictCode>'${dictCode}'<#if dictCode_has_next>,</#if></#list>)
</#if>

const queryForm = reactive<${classNamePrefix}Query>({
<#list fieldConfigs as fieldConfig>
<#if fieldConfig.showInQuery>
  ${fieldConfig.fieldName}: undefined,
</#if>
</#list>
  sort: ['id,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => list${classNamePrefix}({ ...queryForm, ...page }), { immediate: true })
const columns = ref<TableInstanceColumns[]>([
<#if fieldConfigs??>
  <#list fieldConfigs as fieldConfig>
  <#if fieldConfig.showInList>
   <#if fieldConfig.fieldName=="createUser" >
  { title: '${fieldConfig.comment}', dataIndex: 'createUserString', slotName: '${fieldConfig.fieldName}' },
   <#elseif fieldConfig.fieldName=="updateUser"  >
  { title: '${fieldConfig.comment}', dataIndex: 'updateUserString', slotName: '${fieldConfig.fieldName}' },
  <#else>
  { title: '${fieldConfig.comment}', dataIndex: '${fieldConfig.fieldName}', slotName: '${fieldConfig.fieldName}' },
  </#if>
</#if>
</#list>
</#if>
  {
    title: '操作',
    dataIndex: 'action',
    slotName: 'action',
    width: 160,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['${apiModuleName}:${apiName}:detail', '${apiModuleName}:${apiName}:update', '${apiModuleName}:${apiName}:delete'])
  }
]);

// 重置
const reset = () => {
<#list fieldConfigs as fieldConfig>
<#if fieldConfig.showInQuery>
  queryForm.${fieldConfig.fieldName} = undefined
</#if>
</#list>
  search()
}

// 删除
const onDelete = (record: ${classNamePrefix}Resp) => {
  return handleDelete(() => delete${classNamePrefix}(record.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => export${classNamePrefix}(queryForm))
}

const ${classNamePrefix}AddModalRef = ref<InstanceType<typeof ${classNamePrefix}AddModal>>()
// 新增
const onAdd = () => {
  ${classNamePrefix}AddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (record: ${classNamePrefix}Resp) => {
  ${classNamePrefix}AddModalRef.value?.onUpdate(record.id)
}

const ${classNamePrefix}DetailDrawerRef = ref<InstanceType<typeof ${classNamePrefix}DetailDrawer>>()
// 详情
const onDetail = (record: ${classNamePrefix}Resp) => {
  ${classNamePrefix}DetailDrawerRef.value?.onOpen(record.id)
}
</script>

<style scoped lang="scss"></style>
