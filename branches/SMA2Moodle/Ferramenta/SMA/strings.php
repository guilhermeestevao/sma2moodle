<?php

	include_once "/var/www/moodle/config.php";

	function getLanguage(){

		$lang = null;

		if(!isset($_SESSION['SESSION']->lang)) $lang = $_SESSION['USER']->lang;
		else $lang = ($_SESSION['SESSION']->lang);

		return $lang;

	}

	function getString($string){

		$strings = array('en' => array(

			'files_insert_file' => 'Insert File',
			'files_my_files' => 'My Files',
			'files_select_course' => 'Select Course',
			'files_select_activity' => 'Select Activity',
			'files_select_subject' => 'Select Subject',
			'files_add_subject' => 'Append Subject',
			'files_submit_type' => 'Type',
			'cancel' => 'Cancel',
			'course' => 'Course',
			'files_submit_subject' => 'Append Subject',
			'files_open_link' => 'Open Link',
			'files_download_file' => 'Download File',
			'go_back' => 'Back',
			'edit' => 'Edit',
			'delete' => 'Delete',
			'files_name' => 'Name',
			'files_access_option' => 'Access Options',
			'files_manager_options' => 'Manager Options',
			'submit' => 'Submit',
			'file_link' => 'Link',
			'file_file' => 'File',
			'title' => 'Title',
			'control_couse_name' => 'Course Name',
			'messages_new_message' => 'New Message'

		), 'pt_br' => array(

			'files_insert_file' => 'Adicionar Arquivo',
			'files_my_files' => 'Meus arquivos',
			'files_select_course' => 'Selecione o Curso',
			'files_select_activity' => 'Selecione a Atividade',
			'files_select_subject' => 'Selecione o Assunto',
			'files_add_subject' => 'Adicionar Assunto',
			'files_submit_type' => 'Tipo',
			'cancel' => 'Cancelar',
			'course' => 'Curso',
			'files_submit_subject' => 'Adicionar Assunto',
			'files_open_link' => 'Abrir Link',
			'files_download_file' => 'Baixar Arquivo',
			'go_back' => 'Voltar',
			'edit' => 'Editar',
			'delete' => 'Deletar',
			'files_name' => 'Nome',
			'files_access_option' => 'Opções de acesso',
			'files_manager_options' => 'Opções de gerenciamento',
			'submit' => 'Concluir',
			'file_link' => 'Link',
			'file_file' => 'Arquivo',
			'title' => 'Título',
			'control_course_name' => 'Nome do Curso',
			'messages_new_message' => 'Nova Mensagem'

		));

		$lang = getLanguage();
		return $strings[$lang][$string];
#		return var_dump($lang);
	}

?>