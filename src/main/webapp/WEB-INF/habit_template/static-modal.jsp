<%--
  Created by IntelliJ IDEA.
  User: NAMBACH
  Date: 7/1/2018
  Time: 9:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<button id="btnStaticModal" type="button" class="btn btn-secondary mb-1 hidden" data-toggle="modal"
        data-target="#staticModal">
    Open Modal
</button>

<div class="modal fade" id="staticModal" tabindex="-1" role="dialog" aria-labelledby="staticModalLabel"
     data-backdrop="static" style="display: none;" aria-hidden="true">
    <div class="modal-dialog modal-md" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="staticModalLabel">Static Modal</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">

                <form id="staticModalForm">
                    <input id="inputId" class="form-control" type="hidden">

                    <div class="form-group">
                        <label for="inputTitle" class="form-control-label">
                            Title
                        </label>
                        <input id="inputTitle" class="form-control" type="text">
                    </div>

                    <div class="form-group">
                        <label for="inputDescription" class="form-control-label">
                            Description
                        </label>
                        <input id="inputDescription" class="form-control" type="text">
                    </div>

                    <div class="form-group">
                        <label for="inputIcon" class="form-control-label">
                            Icon
                        </label>
                        <input id="inputIcon" class="form-control" type="text" value="coffee">
                    </div>

                    <div class="form-group">
                        <label for="selectRepetition" class="form-control-label">
                            Repetition
                        </label>
                        <select name="select" id="selectRepetition" class="form-control"></select>
                    </div>

                    <div id="times-group" class="form-group hidden">
                        <label for="selectTimes" class="form-control-label">
                            Times
                        </label>
                        <select name="times[]" id="selectTimes" class="form-control" multiple="multiple"></select>
                        <div id="inputYearly" class="hidden" style="display: block">
                            <select name="date" class="form-control"
                                    style="display: inline; width: 48%; margin: 0 0%"></select>
                            <select name="month" class="form-control"
                                    style="display: inline; width: 48%; margin: 0 1%"></select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="inputFrom" class="form-control-label">
                            From
                        </label>
                        <div id="inputFrom" style="display: block">
                            <select name="hour" class="form-control"
                                    style="display: inline; width: 48%; margin: 0 0%"></select>
                            <select name="minute" class="form-control"
                                    style="display: inline; width: 48%; margin: 0 1%"></select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="inputTo" class="form-control-label">
                            To
                        </label>
                        <div id="inputTo" style="display: block">
                            <select name="hour" class="form-control"
                                    style="display: inline; width: 48%; margin: 0 0%"></select>
                            <select name="minute" class="form-control"
                                    style="display: inline; width: 48%; margin: 0 1%"></select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="inputTags" class="form-control-label">
                            Tags
                        </label>
                        <input id="inputTags" class="form-control" type="text">
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button id="btnSubmit" type="button" class="btn btn-primary">Add</button>
                <button id="btnSubmitUpdate" type="button" class="hidden btn btn-primary">Update</button>
            </div>
        </div>
    </div>
</div>
