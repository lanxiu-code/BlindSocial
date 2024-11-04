<template>
     <Form  autoComplete='off'>
      <FormItem label='Username'>
        <Input placeholder='please enter your username...' />
      </FormItem>
      <FormItem label='Post'>
        <Input placeholder='please enter your post...' />
      </FormItem>
      <FormItem wrapperCol={{ offset: 5 }}>
        <Checkbox>I have read the manual</Checkbox>
      </FormItem>
      <FormItem wrapperCol={{ offset: 5 }}>
        <Button type='primary'>Submit</Button>
      </FormItem>
    </Form>
</template>

<script setup lang="ts"></script>

<style lang="scss" scoped>
.adminPostForm {
}
</style>
